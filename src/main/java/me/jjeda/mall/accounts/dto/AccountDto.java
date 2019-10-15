package me.jjeda.mall.accounts.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.domain.AccountStatus;
import me.jjeda.mall.common.model.Address;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

//@JsonIgnoreProperties({"email", "password", "phone", "address", "accountRole"})
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountDto {

    private Long id;
    @NotNull
    private String nickname;
    /**
     * email, password, phone, Role, address 등은 개인정보로 직렬화하여 메시지에 담지 않는다.
     */
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    private Set<AccountRole> accountRole;

    private Address address;

    private LocalDateTime createAt;

    private AccountStatus status;

    public Account toEntity() {

        if (Objects.isNull(this.id)) {
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
        return Account.builder()
                .id(this.id)
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .address(this.address)
                .phone(this.phone)
                .accountRole(this.accountRole)
                .createdAt(this.createAt)
                .status(this.status)
                .build();
    }
}
