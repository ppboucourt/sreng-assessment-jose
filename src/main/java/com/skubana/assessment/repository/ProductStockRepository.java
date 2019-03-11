package com.skubana.assessment.repository;

import com.skubana.assessment.domain.ProductStock;
import com.skubana.assessment.domain.util.InventoryVO;
import com.skubana.assessment.domain.util.ProductStockVO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductStockRepository extends CrudRepository<ProductStock, Long> {



    @Query("SELECT NEW com.skubana.assessment.domain.util.ProductStockVO( " +
            "product.quantity," +
            "product.sku " +
            ") FROM sku_product_stock product " )
    List<ProductStockVO> findProductStockVO();
}
