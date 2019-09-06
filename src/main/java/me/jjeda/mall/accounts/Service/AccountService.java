package me.jjeda.mall.accounts.Service;

import lombok.AllArgsConstructor;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    AccountRepository accountRepository;

    public Account saveAccount(AccountDto dto) {
        //TODO : passwordEncoder로 비밀번호 hashing처리
        return accountRepository.save(dto.toEntity());
    }
}
