package me.jjeda.mall.items.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.exception.NotEnoughStockException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    /**
     * 판매자가 상품판매를 등록하면 (Repository 를 통해 DB에 저장하면)
     * CascadeType.ALL 옵션을 통해 회원 판매목록과 아이템카테고리에도 DB call
     */

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemCategory> itemCategories;

    public Item(String name, int price, int stockQuantity, Account account) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.account = account;
        account.getItems().add(this);
        this.itemCategories = new ArrayList<>();
    }

    public void addItemCategory(ItemCategory itemCategory) {
        itemCategories.add(itemCategory);
        itemCategory.setItem(this);

        Category category = itemCategory.getCategory();
        category.getItemCategories().add(itemCategory);
    }

    /**
     * 상품을 주문 or 취소 시 stock 감소 or 증가 로직
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
