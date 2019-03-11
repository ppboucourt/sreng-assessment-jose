package com.skubana.assessment.service.impl;

import com.skubana.assessment.domain.Inventory;
import com.skubana.assessment.repository.InventoryRepository;
import com.skubana.assessment.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryImpl implements InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    public void updateFulfillable(Inventory inventory) {
        inventory.setFulfillable(!inventory.isFulfillable());
        inventoryRepository.save(inventory);
    }

    @Override
    public void updateFulfillable(Inventory inventory, Boolean fulfillable) {
        inventory.setFulfillable(fulfillable);
        inventoryRepository.save(inventory);
    }

}
