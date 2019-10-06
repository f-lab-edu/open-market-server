package me.jjeda.mall.items.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.items.domain.Item;
import me.jjeda.mall.items.domain.ItemCategory;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemDto {

    private String name;

    private int price;

    private int stockQuantity;

    private Account account;

    private List<ItemCategory> itemCategories;

    public Item from() {
        return Item.builder()
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .account(this.account)
                .itemCategories(this.itemCategories)
                .build();
    }
}
