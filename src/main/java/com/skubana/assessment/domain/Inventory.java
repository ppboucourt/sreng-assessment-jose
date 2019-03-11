package com.skubana.assessment.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "sku_inventory")
public class Inventory {

	@Id
	@GeneratedValue
	private Long inventoryId;

	@OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
	private List<ProductStock> productStocks;

	@OneToOne
	@JoinColumn(name = "order_id", referencedColumnName = "orderId")
	private Order order;


	private boolean fulfillable;

	public Inventory() {
		this.productStocks = new ArrayList<>();
	}

	public List<ProductStock> getProductStocks() {
		return productStocks;
	}

	public void setProductStocks(List<ProductStock> productStocks) {
		this.productStocks = productStocks;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public boolean isFulfillable() {
		return fulfillable;
	}

	public void setFulfillable(boolean fulfillable) {
		this.fulfillable = fulfillable;
	}
}
