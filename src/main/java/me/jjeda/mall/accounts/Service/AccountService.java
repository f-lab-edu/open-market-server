package me.jjeda.mall.accounts.Service;

import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account saveAccount(AccountDto dto) {
        Account account = dto.toEntity();
        account.setPassword(passwordEncoder.encode(dto.getPassword()));

        return accountRepository.save(account);
    }

    public PagedResources findAllAccountWithValidation(Boolean isDeleted, Pageable pageable, PagedResourcesAssembler<Account> pagedResourcesAssembler) {

        return pagedResourcesAssembler.toResource(accountRepository.findAccountsByIsDeleted(isDeleted, pageable));

    }

    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByAdmin(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setDeleteFlag();
    }

    @Transactional
    public void deleteAccountByAdmin(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setDeleteFlag();
    }

    @Transactional
    public Account updateAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).get();
        account.update(accountDto);

        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(account.getEmail(), account.getPassword(), authorities(account.getAccountRole()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }
}
