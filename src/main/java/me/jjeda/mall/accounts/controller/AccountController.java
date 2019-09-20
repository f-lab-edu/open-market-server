package me.jjeda.mall.accounts.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountStatus;
import me.jjeda.mall.accounts.dto.AccountAdapter;
import me.jjeda.mall.accounts.dto.AccountDto;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto requestAccount) {
        Account account = accountService.saveAccount(requestAccount);
        URI uri = ControllerLinkBuilder.linkTo(AccountController.class).slash(account.getId()).toUri();

        return ResponseEntity.created(uri).body(account);
    }

    @GetMapping
    public ResponseEntity getAccount(@AuthenticationPrincipal AccountAdapter accountAdapter) {
        Account account = accountAdapter.getAccount();

        return ResponseEntity.ok(account);
    }

    @DeleteMapping
    public ResponseEntity deleteAccount(@AuthenticationPrincipal AccountAdapter accountAdapter) {
        Account account = accountAdapter.getAccount();
        accountService.changeAccountStatus(account.getId(), AccountStatus.DELETED);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updateAccount(@RequestBody @Valid AccountDto accountDto, @AuthenticationPrincipal AccountAdapter accountAdapter) {
        Account account = accountAdapter.getAccount();

        account = accountService.updateAccount(account.getId(), accountDto);

        return ResponseEntity.ok(account);
    }
}
