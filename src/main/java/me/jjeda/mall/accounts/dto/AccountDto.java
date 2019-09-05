package me.jjeda.mall.accounts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.domain.Address;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"email", "password", "phone", "address", "accountRole"})
public class AccountDto {
    private Long id;

    private String userName;

    /**
     * email, password, phone, Role, address 등은 개인정보로 직렬화하여 메시지에 담지 않는다.
     */
    private String email;

    private String password;

    private String phone;

    private AccountRole accountRole;

    private Address address;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
