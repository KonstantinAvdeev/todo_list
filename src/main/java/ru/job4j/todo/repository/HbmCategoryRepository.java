package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HbmCategoryRepository implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Category> findAll() {
        return crudRepository.query("FROM Category", Category.class);
    }

    @Override
    public List<Category> findById(List<Integer> categoriesId) {
        return crudRepository.query("FROM Category WHERE id IN (:idList)", Category.class,
                Map.of("idList", categoriesId));
    }
}
