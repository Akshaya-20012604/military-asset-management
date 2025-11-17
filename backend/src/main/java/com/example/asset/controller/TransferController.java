package com.example.asset.controller;

import com.example.asset.entity.InventoryMovement;
import com.example.asset.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final InventoryService inventoryService;
    private final com.example.asset.security.JwtUtil jwtUtil;

    public TransferController(InventoryService inventoryService, com.example.asset.security.JwtUtil jwtUtil) {
        this.inventoryService = inventoryService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestBody Map<String,Object> body, Principal principal) {
        Long fromBaseId = Long.valueOf(String.valueOf(body.get("fromBaseId")));
        Long toBaseId = Long.valueOf(String.valueOf(body.get("toBaseId")));
        Long equipmentTypeId = Long.valueOf(String.valueOf(body.get("equipmentTypeId")));
        int qty = Integer.parseInt(String.valueOf(body.get("quantity")));
        String reference = (String) body.getOrDefault("reference", null);
        Long userId = jwtUtil.extractUserIdFromPrincipal(principal);
        InventoryMovement created = inventoryService.createTransfer(fromBaseId, toBaseId, equipmentTypeId, qty, reference, userId);
        return ResponseEntity.status(201).body(created);
    }
}
