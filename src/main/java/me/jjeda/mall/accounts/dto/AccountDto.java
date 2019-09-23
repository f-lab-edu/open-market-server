package me.jjeda.mall.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.domain.AccountStatus;
import me.jjeda.mall.accounts.domain.Address;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

//@JsonIgnoreProperties({"email", "password", "phone", "address", "accountRole"})
@AllArgsConstructor
@Getter
@Builder
public class AccountDto {

    @NotNull
    private String nickname;
    /**
     * email, password, phone, Role, address 등은 개인정보로 직렬화하여 메시지에 담지 않는다.
     */
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    private Set<AccountRole> accountRole;

    private Address address;

    public Account toEntity() {
        return Account.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .address(this.address)
                .phone(this.phone)
                .accountRole(this.accountRole)
                .createdAt(LocalDateTime.now())
                .status(AccountStatus.NORMAL)
                .build();
    }
}
