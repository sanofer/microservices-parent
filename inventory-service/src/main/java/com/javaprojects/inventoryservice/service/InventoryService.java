package com.javaprojects.inventoryservice.service;

import com.javaprojects.inventoryservice.dto.InventoryDto;
import com.javaprojects.inventoryservice.dto.InventoryStockResponse;
import com.javaprojects.inventoryservice.model.Inventory;
import com.javaprojects.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    public void createInventory(InventoryDto inventoryDto){
        Inventory inventory = new Inventory();
        inventory.setName(inventoryDto.getName());
        inventory.setSkuCode(inventoryDto.getSkuCode());
        inventory.setDescription(inventoryDto.getDescription());
        inventory.setQuantity(inventoryDto.getQuantity());
        inventoryRepository.save(inventory);
    }
    @Transactional(readOnly = true)
    public List<InventoryStockResponse> isInStock(List<String> skuCodes) {
        List<Inventory>  inventoryList= inventoryRepository.findBySkuCodeIn(skuCodes);
        List<InventoryStockResponse> inventoryStockResponses =  inventoryList.stream()
                .map(inventory ->
                    InventoryStockResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() >0).build()
                ).toList();
        return inventoryStockResponses;
    }

}
