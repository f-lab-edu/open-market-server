package me.jjeda.mall.accounts.controller;

import lombok.AllArgsConstructor;
import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.dto.AccountDto;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;

    @PostMapping
    public ResponseEntity createAccount(@RequestBody AccountDto requestAccount) {
        Account account = accountService.saveAccount(requestAccount);
        URI uri = ControllerLinkBuilder.linkTo(AccountController.class).slash(account.getId()).toUri();

        return ResponseEntity.created(uri).body(account);
    }
}
