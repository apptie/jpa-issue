package com.jpa.issue.repository;

import com.jpa.issue.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c join fetch c.children where c.id = :id")
    Category findParentWithAllChildrenById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"children"})
    Category findChildrenIdWithParentById(Long id);
}
