package com.okanefy.okanefy.repositories;

import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<List<Category>> findAllByUserIdAndStatus(Long userId, int status);

    Optional<List<Category>> findAllByNameContainingIgnoreCaseAndTypeAndUserIdAndStatus(String name, CategoriesTypes type, Long userId, int status);

    Optional<List<Category>> findAllByNameContainingIgnoreCaseAndUserIdAndStatus(String name, Long userId, int status);

    Optional<List<Category>> findAllByTypeAndUserIdAndStatus(CategoriesTypes type, Long userId, int status);
}
