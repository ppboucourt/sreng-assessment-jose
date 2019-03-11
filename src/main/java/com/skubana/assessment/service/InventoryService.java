package com.skubana.assessment.service;

import com.skubana.assessment.domain.Inventory;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {
    void updateFulfillable(Inventory inventory);

    void updateFulfillable(Inventory inventory, Boolean fulfillable);
}
