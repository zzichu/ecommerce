package com.ecomerce.domain.purchase.entity;

import jakarta.persistence.*;
import com.ecomerce.domain.purchase.id.PurchaseItemId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@IdClass(PurchaseItemId.class)
@Table(name = "purchase_item")
public class PurchaseItemEntity {
    @Id
    private Long purchaseId;

    @Id
    private Long itemId;

}
