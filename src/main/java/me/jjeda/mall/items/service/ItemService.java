package me.jjeda.mall.items.service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.items.domain.Item;
import me.jjeda.mall.items.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Optional<Item> getItem(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Transactional(readOnly = true)
    public List<Item> salesList(Long accountId) {
        return itemRepository.findAllByAccountId(accountId);
    }
}
