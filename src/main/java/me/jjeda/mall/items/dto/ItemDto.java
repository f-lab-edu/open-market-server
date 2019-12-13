package me.jjeda.mall.items.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.items.domain.Item;
import me.jjeda.mall.items.domain.ItemCategory;

import java.util.List;

@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ItemDto {

    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private Account account;

    private List<ItemCategory> itemCategories;

    public Item toEntity() {
        return Item.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .account(this.account)
                .itemCategories(this.itemCategories)
                .build();
    }
}
