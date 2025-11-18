package com.ecomerce.domain.purchase.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecomerce.domain.item.repository.ItemRepository;
import com.ecomerce.domain.purchase.dto.PurchaseDetailDto;
import com.ecomerce.domain.purchase.dto.PurchaseItemDto;
import com.ecomerce.domain.purchase.entity.PurchaseDetailEntity;
import com.ecomerce.domain.purchase.entity.PurchaseItemEntity;
import com.ecomerce.domain.purchase.enums.DeliveryStatus;
import com.ecomerce.domain.purchase.repository.PurchaseDetailRepository;
import com.ecomerce.domain.purchase.repository.PurchaseItemRepository;
import com.ecomerce.domain.item.entity.ItemEntity;

@Service
public class PurchaseService {
	
	@Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Autowired
    private PurchaseDetailRepository purchaseDetailRepository;

    @Transactional
    public boolean purchaseItem(PurchaseItemDto purchaseItemDto, PurchaseDetailDto purchaseDetailDto) {

        ItemEntity item = itemRepository.findById(purchaseItemDto.getItemId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        
        PurchaseDetailEntity purchaseDetail = PurchaseDetailEntity.builder()
                .userId(purchaseDetailDto.getUserId())
                .purchaseDate(purchaseDetailDto.getPurchaseDate())
                .deliveryStatus(purchaseDetailDto.getDeliveryStatus())
                .build();
        purchaseDetail = purchaseDetailRepository.save(purchaseDetail);  
        
        PurchaseItemEntity purchaseItem = PurchaseItemEntity.builder()
            .purchaseId(purchaseDetail.getPurchaseId())
            .itemId(purchaseItemDto.getItemId())
            .build();
        purchaseItemRepository.save(purchaseItem);

        return true;
    }


    @Transactional(readOnly = true)
    public List<PurchaseDetailDto> getAllPurchases() {
        return purchaseDetailRepository.findAll().stream()
            .map(detail -> PurchaseDetailDto.builder()
                .purchaseId(detail.getPurchaseId())
                .userId(detail.getUserId())
                .purchaseDate(detail.getPurchaseDate())
                .deliveryStatus(detail.getDeliveryStatus())
                .build())
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseDetailDto> getPurchaseById(Long purchaseId) {
        return purchaseDetailRepository.findById(purchaseId)
            .map(detail -> PurchaseDetailDto.builder()
                .purchaseId(detail.getPurchaseId())
                .userId(detail.getUserId())
                .purchaseDate(detail.getPurchaseDate())
                .deliveryStatus(detail.getDeliveryStatus())
                .build());
    }

    @Transactional
    public boolean updateDeliveryStatus(Long purchaseId, DeliveryStatus newStatus) {
        PurchaseDetailEntity entity = purchaseDetailRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매 내역입니다."));
        entity.changeDeliveryStatus(newStatus);
        purchaseDetailRepository.save(entity);
        return true;
    }

}
