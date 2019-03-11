package com.skubana.assessment.repository;

import com.skubana.assessment.domain.Inventory;
import com.skubana.assessment.domain.util.InventoryVO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    @Query("SELECT inventory FROM sku_inventory inventory " +
            "JOIN FETCH inventory.productStocks " +
            "WHERE inventory.fulfillable = false " )
    List<Inventory> findAllByFulfillableIsFalse();

    @Query("SELECT inventory FROM sku_inventory inventory " +
            "JOIN FETCH inventory.productStocks " +
            "WHERE inventory.fulfillable =:value " )
    List<Inventory> findAllByFulfillable(@Param("value") Boolean value);
}
