package me.jjeda.mall.accounts.domain;

import me.jjeda.mall.accounts.dto.AccountDto;

import java.time.LocalDateTime;
import java.util.Objects;

public class AccountAndDtoAdapter {


    public static AccountDto entityToDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .nickname(account.getNickname())
                .email(account.getEmail())
                .password(account.getPassword())
                .phone(account.getPhone())
                .accountRole(account.getAccountRole())
                .address(account.getAddress())
                .createAt(account.getCreatedAt())
                .status(account.getStatus())
                .build();
    }

    public static Account dtoToEntity(AccountDto accountDto) {

        Account account = Account.builder()
                .nickname(accountDto.getNickname())
                .email(accountDto.getEmail())
                .password(accountDto.getPassword())
                .address(accountDto.getAddress())
                .phone(accountDto.getPhone())
                .accountRole(accountDto.getAccountRole())
                .createdAt(LocalDateTime.now())
                .status(AccountStatus.NORMAL)
                .build();

        if (Objects.nonNull(accountDto.getId())) {
            account.setId(accountDto.getId());
            account.setCreatedAt(accountDto.getCreateAt());
            account.setStatus(accountDto.getStatus());
        }

        return account;
    }
}
