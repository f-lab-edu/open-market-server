package me.jjeda.mall.accounts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.EnumType;
import javax.persistence.Embedded;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    private String email;

    private String password;

    private String phone;

    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    /**
     * 회원(구매측)뿐만아니라 회사(판매측) 등 재사용성을 위해 값타입으로 매핑
     */
    @Embedded
    private Address address;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public void setDeleteFlag() {
        this.isDeleted = true;
    }
}
