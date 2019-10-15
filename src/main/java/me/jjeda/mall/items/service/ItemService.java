package me.jjeda.mall.items.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.exception.NotEnoughStockException;
import me.jjeda.mall.items.domain.Category;
import me.jjeda.mall.items.domain.Item;
import me.jjeda.mall.items.domain.ItemCategory;
import me.jjeda.mall.items.dto.ItemDto;
import me.jjeda.mall.items.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Item saveItem(ItemDto itemDto) {
        Item item = itemDto.toEntity();
        List<ItemCategory> itemCategories = item.getItemCategories();

        for (ItemCategory itemCategory : itemCategories) {
            itemCategory.setItem(item);
            Category category = itemCategory.getCategory();
            category.getItemCategories().add(itemCategory);
        }

        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Optional<Item> getItem(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Transactional(readOnly = true)
    public List<Item> salesList(Long accountId) {
        return itemRepository.findAllByAccountId(accountId);
    }

    /**
     * 상품을 주문 or 취소 시 stock 감소 or 증가 로직
     */
    @Transactional
    public void addStock(Long itemId, int quantity) {
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        item.setStockQuantity(item.getStockQuantity()+quantity);
    }

    @Transactional
    public void removeStock(Long itemId, int quantity) {
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        int restStock = item.getStockQuantity() - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        item.setStockQuantity(restStock);
    }
}
