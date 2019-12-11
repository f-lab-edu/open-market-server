package me.jjeda.mall.accounts.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.domain.AccountStatus;
import me.jjeda.mall.common.model.Address;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

//@JsonIgnoreProperties({"email", "password", "phone", "address", "accountRole"})
@Getter
@Builder
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

}
