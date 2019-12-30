package me.jjeda.mall.cart.controller;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.cart.domain.CartItem;
import me.jjeda.mall.cart.domain.CartModifyType;
import me.jjeda.mall.cart.service.CartService;
import me.jjeda.mall.common.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity getCart(@CurrentUser AccountDto accountDto) {
        return ResponseEntity.ok(cartService.getCart(String.valueOf(accountDto.getEmail())));
    }

    @PutMapping
    public ResponseEntity modifyCart(@CurrentUser AccountDto accountDto, @RequestBody CartItem cartItem,
                                  @RequestParam CartModifyType cartModifyType) {

        if(Objects.equals(cartModifyType,CartModifyType.ADD)) {
            return ResponseEntity.ok(cartService.addItem(String.valueOf(accountDto.getEmail()), cartItem));
        } else {
            return ResponseEntity.ok(cartService.removeItem(String.valueOf(accountDto.getEmail()), cartItem));
        }
    }
    @DeleteMapping
    public ResponseEntity deleteCart(@CurrentUser AccountDto accountDto) {
        cartService.deleteCart(String.valueOf(accountDto.getId()));
        return ResponseEntity.ok().build();
    }
}
