package com.example.asset.service;

import com.example.asset.entity.*;
import com.example.asset.repository.AuditLogRepository;
import com.example.asset.repository.InventoryMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InventoryService {

    private final InventoryMovementRepository movementRepo;
    private final AuditLogRepository auditRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public InventoryService(InventoryMovementRepository movementRepo, AuditLogRepository auditRepo) {
        this.movementRepo = movementRepo;
        this.auditRepo = auditRepo;
    }

    @Transactional
    public InventoryMovement createPurchase(Long baseId, Long equipmentTypeId, int quantity, String reference, Long performedBy) {
        InventoryMovement m = new InventoryMovement();
        m.setKind("PURCHASE");
        m.setQuantity(quantity);
        m.setToBase(new Base(baseId));
        m.setEquipmentType(new EquipmentType(equipmentTypeId));
        m.setReference(reference);
        m.setCreatedBy(performedBy);
        InventoryMovement saved = movementRepo.save(m);
        createAudit("inventory_movements", saved.getId(), "CREATE", saved, performedBy);
        return saved;
    }

    @Transactional
    public InventoryMovement createTransfer(Long fromBaseId, Long toBaseId, Long equipmentTypeId, int quantity, String reference, Long performedBy) {
        InventoryMovement m = new InventoryMovement();
        m.setKind("TRANSFER");
        m.setQuantity(quantity);
        m.setFromBase(new Base(fromBaseId));
        m.setToBase(new Base(toBaseId));
        m.setEquipmentType(new EquipmentType(equipmentTypeId));
        m.setReference(reference);
        m.setCreatedBy(performedBy);
        InventoryMovement saved = movementRepo.save(m);
        createAudit("inventory_movements", saved.getId(), "CREATE", saved, performedBy);
        return saved;
    }

    private void createAudit(String table, Long recordId, String action, Object payloadObj, Long performedBy) {
        try {
            AuditLog al = new AuditLog();
            al.setTableName(table);
            al.setRecordId(recordId);
            al.setAction(action);
            al.setPayload(mapper.writeValueAsString(payloadObj));
            al.setPerformedBy(performedBy);
            auditRepo.save(al);
        } catch (Exception ex) {
            // swallow - audits should not break main flow in this skeleton
        }
    }
}
