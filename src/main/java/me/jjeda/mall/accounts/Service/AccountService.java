package me.jjeda.mall.accounts.Service;

import lombok.AllArgsConstructor;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    public Account saveAccount(AccountDto dto) {
        Account account = dto.toEntity();
        account.setPassword(passwordEncoder.encode(dto.getPassword()));

        return accountRepository.save(account);
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
    }

    public void deleteAccountByAdmin(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setDeleteFlag();
    }

    public Account updateAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).get();
        account.update(accountDto);

        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        //TODO : account 객체를 UserDetails에 맞게 리턴 -> USER 클래스를 사용할까?
        return null;
    }
}
