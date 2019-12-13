package me.jjeda.mall.accounts.domain;

import me.jjeda.mall.accounts.dto.AccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountAndUserAdapter extends User {

    private final AccountDto accountDto;

    private AccountAndUserAdapter(AccountDto accountDto) {
        super(accountDto.getEmail(), accountDto.getPassword(), authorities(accountDto.getAccountRole()));
        this.accountDto = accountDto;
    }

    public static AccountAndUserAdapter from(AccountDto accountDto) {
        return new AccountAndUserAdapter(accountDto);
    }

    private static Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }

    public AccountDto getAccountDto() {
        return this.accountDto;
    }
}
