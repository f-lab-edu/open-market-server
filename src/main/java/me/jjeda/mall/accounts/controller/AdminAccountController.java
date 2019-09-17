package me.jjeda.mall.accounts.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.domain.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin/accounts")
public class AdminAccountController {

    private AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 유효 or 유효하지 않은 Account 조회하는 method
     * @param isDeleted : Account 가 삭제됐을 때 true 값을 갖는 flag
     */
    @GetMapping
    public ResponseEntity getAllAccountWithValidation(@RequestParam Boolean isDeleted, Pageable pageable, PagedResourcesAssembler<Account> pagedResourcesAssembler) {
        PagedResources pagedResources = accountService.findAllAccountWithValidation(isDeleted, pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAccountByAdmin(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountByAdmin(id);
        if (account.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccountByAdmin(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountByAdmin(id);
        if (account.isEmpty()) return ResponseEntity.notFound().build();

        accountService.deleteAccountByAdmin(id);

        return ResponseEntity.ok(account);
    }
}
