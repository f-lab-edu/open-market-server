package me.jjeda.mall.accounts.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountStatus;
import me.jjeda.mall.accounts.dto.AccountDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/admin/accounts")
public class AdminAccountController {

    private final AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity getAccountWithStatus(@RequestParam AccountStatus status,
                                               Pageable pageable,
                                               PagedResourcesAssembler<Account> pagedResourcesAssembler) {
        PagedResources pagedResources
                = accountService.findAllAccountWithStatus(status, pageable, pagedResourcesAssembler);

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) {
        AccountDto accountDto;

        try {
            accountDto = accountService.getAccount(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(accountDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity changeAccountStatus(@PathVariable Long id, @RequestParam AccountStatus status) {
        AccountDto accountDto;

        try {
            accountDto = accountService.getAccount(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        if(status.equals(AccountStatus.DELETED)) {
            //관리자는 탈퇴 처리 할 수없다.
            return ResponseEntity.badRequest().build();
        }
        accountService.changeAccountStatus(id,status);
        return ResponseEntity.ok(accountService.getAccount(accountDto.getId()));
    }
}
