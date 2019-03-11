package com.skubana.assessment;

import com.skubana.assessment.domain.Inventory;
import com.skubana.assessment.domain.Order;

import com.skubana.assessment.domain.OrderItem;
import com.skubana.assessment.domain.ProductStock;
import com.skubana.assessment.domain.util.InventoryVO;
import com.skubana.assessment.domain.util.ProductStockVO;
import com.skubana.assessment.repository.InventoryRepository;
import com.skubana.assessment.repository.OrderItemRepository;
import com.skubana.assessment.repository.OrderRepository;
import com.skubana.assessment.repository.ProductStockRepository;
import com.skubana.assessment.service.InventoryService;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class AssessmentController {

	private final Logger log = LoggerFactory.getLogger(AssessmentController.class);

	private static final String MARKETPLATE_ENDPOINT_URL = "http://assessment.skubana.com/orders";
	private static final String MARKETPLATE_PRODUCT_STOCK_ENDPOINT_URL = "http://assessment.skubana.com/products/stocks";
	private static final String MARKETPLATE_SHIPMENTS_ENDPOINT_URL = "http://assessment.skubana.com/products/shipments";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository itemRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ProductStockRepository productStockRepository;

	@Autowired
	private InventoryService inventoryService;

	@Scheduled(fixedDelay = 10000)
	public void getOrders() {
		try {
			List<Order> orderList = restTemplate.exchange(MARKETPLATE_ENDPOINT_URL,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<List<Order>>() {
					})
					.getBody();

			// set orderItem->order reference then save
			orderList.stream()
					.forEach(order -> {
						order.getOrderItems().stream()
								.forEach(orderItem -> orderItem.setOrder(order));
						orderRepository.save(order);
					});

			System.out.println(orderList.size() + " orders downloaded");
			//Prepare all the information in the database
			prepareData();
		} catch (Exception e) {
			log.info(e.getMessage());
		}
//		return orderList;
	}

	@Scheduled(fixedDelay = 5000)
	private void checkForShipment() {
		shipProducts();
	}

	@Scheduled(fixedDelay = 5000)
	private void checkForFulfillOrder() {
		allocateProducts();
	}

	private void prepareData() {
		List<Order> orders = (List<Order>) orderRepository.findAll();
		if(orders.size() > 0) {
			log.info("Filling th information for the Inventory");
			for (Order order: orders) {
				Inventory inventory = new Inventory();
				inventory.setOrder(order);
				inventory.setFulfillable(false);
				for (OrderItem item: order.getOrderItems()) {
					ProductStock product = new ProductStock(item.getQuantity(), item.getSku());
					product.setInventory(inventory);
					inventory.getProductStocks().add(product);
				}
				inventoryRepository.save(inventory);
			}
		}
	}

	@Async
	void allocateProducts() {
		List<Inventory> inventories = inventoryRepository.findAllByFulfillableIsFalse();
//			InventoryVO inventoryVO = new InventoryVO(productStockRepository.findProductStockVO());
		if(inventories.size() > 0) {
			try {
				for (Inventory inventory: inventories) {
					InventoryVO inventoryVO = new InventoryVO();
					List<ProductStockVO> productStockVOS = fillProductsStockVO(inventory.getProductStocks());
					inventoryVO.getProductStocks().addAll(productStockVOS);

					HttpEntity<InventoryVO> request = new HttpEntity<>(inventoryVO);
					ResponseEntity<HttpResponse> response =
							restTemplate.exchange(MARKETPLATE_PRODUCT_STOCK_ENDPOINT_URL,
									HttpMethod.POST,
									request,
									HttpResponse.class);
					if(response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCodeValue() == 200)
						inventoryService.updateFulfillable(inventory, Boolean.TRUE);
					else
					if(response.getStatusCode() == HttpStatus.BAD_REQUEST || response.getStatusCodeValue() == 400)
						inventoryService.updateFulfillable(inventory, Boolean.FALSE);

				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
	}

	private List<ProductStockVO> fillProductsStockVO(List<ProductStock> productStocks) {
		List<ProductStockVO> productStockVOS = new ArrayList<>();
		for (ProductStock product: productStocks) {
			productStockVOS.add(new ProductStockVO(product.getQuantity(), product.getSku()));
		}
		return productStockVOS;
	}

	public void shipProducts() {
		List<Inventory> inventories = inventoryRepository.findAllByFulfillable(Boolean.TRUE);

		for (Inventory inventory: inventories) {
			HttpEntity<String> request = new HttpEntity<>(inventory.getOrder().getOrderNumber());
			ResponseEntity<HttpResponse> response =
					restTemplate.exchange(MARKETPLATE_SHIPMENTS_ENDPOINT_URL,
							HttpMethod.POST,
							request,
							HttpResponse.class);
			if(response.getStatusCode() == HttpStatus.ACCEPTED ||response.getStatusCodeValue() == 200)
				System.out.println("The order with number " + inventory.getOrder().getOrderNumber() + " have been shipped successfully " + response.getStatusCode());
			else {
				log.info(response.getStatusCode().getReasonPhrase());
				System.out.println("The intent to ship the order " + inventory.getOrder().getOrderNumber() + " have failed " + response.getStatusCode());
			}
		}
		log.info("Shipping...");
	}

}
