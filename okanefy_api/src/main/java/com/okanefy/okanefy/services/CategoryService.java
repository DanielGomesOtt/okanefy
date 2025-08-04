package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.category.CategoriesListDTO;
import com.okanefy.okanefy.dto.category.CreatedCategoryDTO;
import com.okanefy.okanefy.dto.category.NewCategoryDTO;
import com.okanefy.okanefy.dto.category.UpdateCategoryDTO;
import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.models.Category;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.CategoryRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public CreatedCategoryDTO save(NewCategoryDTO category) {
        Users user = usersRepository.findById(category.user_id()).get();
        Category newCategory = new Category(category, user);
        return new CreatedCategoryDTO(repository.save(newCategory));
    }

    @Transactional
    public void delete(Long id) {
        Optional<Category> category = repository.findById(id);
        category.ifPresent(value -> value.setStatus(0));
    }

    @Transactional
    public CreatedCategoryDTO update(UpdateCategoryDTO data) {
        Optional<Category> category = repository.findById(data.id());

        if(category.isPresent()) {
            category.get().setName(data.name());
            category.get().setType(CategoriesTypes.valueOf(data.type().toUpperCase()));
            return new CreatedCategoryDTO(category.get());
        }

        return null;
    }

    public List<CategoriesListDTO> findAll(Long userId, String name, String type) {
        Optional<List<Category>> categories;
        if(name != null && type != null) {
            categories = repository.findAllByNameContainingIgnoreCaseAndTypeAndUserIdAndStatus(name, CategoriesTypes.valueOf(type.toUpperCase()), userId, 1);
        } else if(name != null) {
            categories = repository.findAllByNameContainingIgnoreCaseAndUserIdAndStatus(name, userId, 1);
        } else if(type != null) {
            categories = repository.findAllByTypeAndUserIdAndStatus(CategoriesTypes.valueOf(type.toUpperCase()), userId, 1);
        } else {
            categories = repository.findAllByUserIdAndStatus(userId, 1);
        }

        return categories.map(categoryList -> categoryList
                .stream()
                .map(CategoriesListDTO::new).toList()).orElseGet(List::of);
    }
}
