package com.skubana.assessment.domain.util;

import com.skubana.assessment.domain.ProductStock;

import java.util.ArrayList;
import java.util.List;

public class InventoryVO {

    private List<ProductStockVO> productStocks;

    public InventoryVO() {
        productStocks = new ArrayList<>();
    }

    public InventoryVO(List<ProductStockVO> productStocks) {
        this.productStocks = productStocks;
    }

    public List<ProductStockVO> getProductStocks() {
        return productStocks;
    }

    public void setProductStocks(List<ProductStockVO> productStocks) {
        this.productStocks = productStocks;
    }
}
