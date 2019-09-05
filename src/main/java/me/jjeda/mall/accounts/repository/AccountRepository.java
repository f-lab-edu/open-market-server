package me.jjeda.mall.accounts.repository;

import me.jjeda.mall.accounts.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
