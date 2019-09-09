package me.jjeda.mall.accounts.Service;

import lombok.AllArgsConstructor;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    AccountRepository accountRepository;

    public Account saveAccount(AccountDto dto) {
        //TODO : passwordEncoder로 비밀번호 hashing처리
        return accountRepository.save(dto.toEntity());
    }

    public PagedResources findAllAccountWithValidation(Boolean isDeleted,Pageable pageable, PagedResourcesAssembler<Account> pagedResourcesAssembler) {

        return pagedResourcesAssembler.toResource(accountRepository.findAccountsByIsDeleted(isDeleted, pageable));

    }

    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByAdmin(Long id) {
        return accountRepository.findById(id);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setDeleteFlag();
        accountRepository.save(account);
    }

    public void deleteAccountByAdmin(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setDeleteFlag();
        accountRepository.save(account);
    }
}
