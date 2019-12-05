package me.jjeda.mall.accounts.Service;

import lombok.RequiredArgsConstructor;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountAndDtoAdapter;
import me.jjeda.mall.accounts.domain.AccountAndUserAdapter;
import me.jjeda.mall.accounts.domain.AccountStatus;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountDto saveAccount(AccountDto dto) {
        Account account = AccountAndDtoAdapter.DtoToEntity(dto);
        account.setPassword(passwordEncoder.encode(dto.getPassword()));

        return AccountAndDtoAdapter.entityToDto(accountRepository.save(account));
    }

    public PagedResources findAllAccountWithStatus(AccountStatus status, Pageable pageable, PagedResourcesAssembler<Account> pagedResourcesAssembler) {

        return pagedResourcesAssembler.toResource(accountRepository.findAccountsByStatus(status, pageable));

    }

    public AccountDto getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return AccountAndDtoAdapter.entityToDto(account);
    }

    @Transactional
    public void changeAccountStatus(Long id, AccountStatus status) {
        Account account = accountRepository.findById(id).get();
        account.setStatus(status);
    }

    @Transactional
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).get();
        account.update(accountDto);

        return AccountAndDtoAdapter.entityToDto(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return AccountAndUserAdapter.from(AccountAndDtoAdapter.entityToDto(account));
    }

}
