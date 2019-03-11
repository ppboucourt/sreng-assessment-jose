
package com.skubana.assessment.domain;

import javax.persistence.*;

@Entity(name = "sku_product_stock")
public class ProductStock {

	@Id
	@GeneratedValue
	private Long productStockId;

	private Integer quantity;

	private String sku;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventoryId", nullable = false)
	private Inventory inventory;

	public ProductStock(Integer quantity, String sku) {
		this.quantity = quantity;
		this.sku = sku;
	}

	public ProductStock() {
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}