package com.ecomerce.domain.purchase.service;

import java.time.LocalDateTime;
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
import com.ecomerce.domain.item.dto.ItemDto;
import com.ecomerce.domain.item.entity.ItemEntity;

@Service
public class PurchaseService {
    
    private final ItemRepository itemRepository;

    public PurchaseService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Autowired
    private PurchaseDetailRepository purchaseDetailRepository;

    @Transactional
    public boolean purchaseItem(PurchaseItemDto purchaseItemDto, PurchaseDetailDto purchaseDetailDto) {
	    Optional<ItemEntity> itemOpt = itemRepository.findById(purchaseItemDto.getItemId());
	    if (itemOpt.isEmpty()) {
	    	return false;
	    } //TODO: orElseThrow.. supply방식 찾아보기 자바 optional 처리 방식

	    PurchaseItemEntity purchaseItem = PurchaseItemEntity.builder()
	    				.purchaseId(purchaseItemDto.getPurchaseId())
	    				.itemId(purchaseItemDto.getItemId())
	    				.build();
	    purchaseItemRepository.save(purchaseItem);
	    
	    // DTO -> Entity로 변환
	    PurchaseDetailEntity purchaseDetail = PurchaseDetailEntity.builder()
	    				.purchaseId(purchaseDetailDto.getPurchaseId())
	    				.userId(purchaseDetailDto.getUserId())
	        			.purchaseDate(purchaseDetailDto.getPurchaseDate())
	        			.deliveryStatus(purchaseDetailDto.getDeliveryStatus())
	        			.build();
	    purchaseDetailRepository.save(purchaseDetail);

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
        Optional<PurchaseDetailEntity> optionalDetail = purchaseDetailRepository.findById(purchaseId);
        if (optionalDetail.isEmpty()) {
            return false;
        } // TODO: orElseThrow..

        PurchaseDetailEntity detail = optionalDetail.get();
        detail.setDeliveryStatus(newStatus); // TODO: set말고 entity메소드사용
        purchaseDetailRepository.save(detail);

        return true;
    }

}
