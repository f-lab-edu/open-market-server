package me.jjeda.mall.accounts.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.dto.AccountDto;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //TODO : 현재 로그인 된 사용자 정보를 불러와 조회, 삭제, 수정 하려는 유저가 아니라면 401 Message 를 리턴하게 하는 로직 추가

    @GetMapping("/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccount(id);
        if (account.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccount(id);
        if (account.isEmpty()) return ResponseEntity.notFound().build();

        accountService.deleteAccount(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAccount(@PathVariable Long id, @RequestBody @Valid AccountDto accountDto) {
        Optional<Account> optionalAccount = accountService.getAccount(id);
        if (optionalAccount.isEmpty()) return ResponseEntity.notFound().build();

        Account account = accountService.updateAccount(id, accountDto);

        return ResponseEntity.ok(account);
    }
}
