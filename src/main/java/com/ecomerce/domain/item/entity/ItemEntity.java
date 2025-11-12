package com.ecomerce.domain.item.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name", nullable = false, length = 255)
    private String itemName;

    @Column(name="item_price", nullable = false)
    private int itemPrice;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "item_image_url", length = 500)
    private String itemImageUrl;

    @Column(name = "creation_user", length = 100)
    private String creationUser;

    @Column(name = "created_date")
    @CreatedBy
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modification_user", length = 100)
    private String modificationUser;


    @Column(name = "modified_date")
    @LastModifiedBy
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "deleted_status")
    private Boolean deletedStatus = false;

}
