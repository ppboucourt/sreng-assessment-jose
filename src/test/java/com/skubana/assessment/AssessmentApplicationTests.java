package com.skubana.assessment;

import com.skubana.assessment.domain.Inventory;
import com.skubana.assessment.domain.ProductStock;
import com.skubana.assessment.domain.util.InventoryVO;
import com.skubana.assessment.domain.util.ProductStockVO;
import com.skubana.assessment.repository.InventoryRepository;
import com.skubana.assessment.repository.ProductStockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssessmentApplicationTests {

	@Autowired
	InventoryRepository inventoryRepository;

	@Autowired
	ProductStockRepository productStockRepository;

	@Autowired
	AssessmentController assessmentController;

	@Test
	public void contextLoads() {
	}

	@Test
	public void InventoryRepository() {
		List<ProductStockVO> result = productStockRepository.findProductStockVO();
		System.out.println(result.toArray().toString());
	}

	@Test
	public void allocateProducts() {
		InventoryVO inventoryVO = new InventoryVO();
		ProductStockVO prod = new ProductStockVO(5, "pepe");
		inventoryVO.getProductStocks().add(prod);
		assessmentController.allocateProducts(inventoryVO);
	}
}
