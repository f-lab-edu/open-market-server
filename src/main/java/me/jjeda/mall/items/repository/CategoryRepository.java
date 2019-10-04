package me.jjeda.mall.items.repository;

import me.jjeda.mall.items.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
