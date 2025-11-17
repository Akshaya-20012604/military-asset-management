package com.example.asset.controller;

import com.example.asset.entity.InventoryMovement;
import com.example.asset.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final InventoryService inventoryService;
    private final com.example.asset.security.JwtUtil jwtUtil;

    public PurchaseController(InventoryService inventoryService, com.example.asset.security.JwtUtil jwtUtil) {
        this.inventoryService = inventoryService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody Map<String,Object> body, Principal principal) {
        Long baseId = Long.valueOf(String.valueOf(body.get("baseId")));
        Long equipmentTypeId = Long.valueOf(String.valueOf(body.get("equipmentTypeId")));
        int qty = Integer.parseInt(String.valueOf(body.get("quantity")));
        String reference = (String) body.getOrDefault("reference", null);
        Long userId = jwtUtil.extractUserIdFromPrincipal(principal);
        InventoryMovement created = inventoryService.createPurchase(baseId, equipmentTypeId, qty, reference, userId);
        return ResponseEntity.status(201).body(created);
    }
}
