package com.skubana.assessment.domain.util;

public class ProductStockVO {

    private Integer quantity;
    private String sku;

    public ProductStockVO(Integer quantity, String sku) {
        this.quantity = quantity;
        this.sku = sku;
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
}
