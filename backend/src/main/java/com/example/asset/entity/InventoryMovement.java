package com.example.asset.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kind; // PURCHASE, TRANSFER, ASSIGNMENT, EXPENDITURE, ADJUSTMENT

    @ManyToOne @JoinColumn(name = "equipment_type_id")
    private EquipmentType equipmentType;

    private int quantity;

    @ManyToOne @JoinColumn(name = "from_base_id")
    private Base fromBase;

    @ManyToOne @JoinColumn(name = "to_base_id")
    private Base toBase;

    private String reference;
    private String notes;
    private Instant timestamp = Instant.now();
    private Long createdBy;
    private Instant createdAt = Instant.now();

    public InventoryMovement() {}

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }
    public EquipmentType getEquipmentType() { return equipmentType; }
    public void setEquipmentType(EquipmentType equipmentType) { this.equipmentType = equipmentType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Base getFromBase() { return fromBase; }
    public void setFromBase(Base fromBase) { this.fromBase = fromBase; }
    public Base getToBase() { return toBase; }
    public void setToBase(Base toBase) { this.toBase = toBase; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
