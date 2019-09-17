package me.jjeda.mall.accounts.controller;

import lombok.AllArgsConstructor;
import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.dto.AccountDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity createAccount(@RequestBody AccountDto requestAccount) {
        Account account = accountService.saveAccount(requestAccount);
        URI uri = ControllerLinkBuilder.linkTo(AccountController.class).slash(account.getId()).toUri();

        return ResponseEntity.created(uri).body(account);
    }

    /**
     * 유효 or 유효하지 않은 Account 조회하는 method
     * @param isDeleted : Account 가 삭제됐을 때 true 값을 갖는 flag
     */
    @GetMapping("/admin")
    public ResponseEntity getAllAccountWithValidation(@RequestParam Boolean isDeleted, Pageable pageable, PagedResourcesAssembler<Account> pagedResourcesAssembler) {
        PagedResources pagedResources = accountService.findAllAccountWithValidation(isDeleted, pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccount(id);

        return ResponseEntity.ok(account);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity getAccountByAdmin(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountByAdmin(id);

        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity deleteAccountByAdmin(@PathVariable Long id) {
        accountService.deleteAccountByAdmin(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        Account account = accountService.updateAccount(id,accountDto);

        return ResponseEntity.ok(account);
    }
}
