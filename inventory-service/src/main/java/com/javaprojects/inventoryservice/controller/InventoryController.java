package com.javaprojects.inventoryservice.controller;

import com.javaprojects.inventoryservice.dto.InventoryDto;
import com.javaprojects.inventoryservice.dto.InventoryStockResponse;
import com.javaprojects.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody InventoryDto inventoryDto){
        inventoryService.createInventory(inventoryDto);
        return "product added to the inventory successfully";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody()
    public List<InventoryStockResponse> isInStock(@RequestParam List<String> skuCodes){
        List<InventoryStockResponse> isd= inventoryService.isInStock(skuCodes);
        return isd;
    }
}
