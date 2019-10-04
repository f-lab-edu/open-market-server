package me.jjeda.mall.items.repository;

import me.jjeda.mall.items.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByAccountId(Long accountId);
}
