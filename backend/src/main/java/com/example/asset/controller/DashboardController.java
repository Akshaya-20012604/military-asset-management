package com.example.asset.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/summary")
    public ResponseEntity<?> summary(@RequestParam(required = false) String start,
                                     @RequestParam(required = false) String end,
                                     @RequestParam(required = false) Long baseId,
                                     @RequestParam(required = false) Long equipmentTypeId) {
        // For skeleton we return sample static values. Replace with real service in full implementation.
        return ResponseEntity.ok(Map.of(
            "openingBalance", 100,
            "netMovement", 20,
            "closingBalance", 120,
            "breakdown", Map.of("purchases",30,"transferIn",10,"transferOut",5,"assignments",10,"expenditures",5)
        ));
    }
}
