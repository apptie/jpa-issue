package com.jpa.issue.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.jpa.issue.IsolateDatabase;
import com.jpa.issue.entity.Category;
import com.jpa.issue.repository.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@Transactional
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestExecutionListeners(value = {DatabaseCleanListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@IsolateDatabase
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void test1() {
        categoryService.addParentCategory("a");

        final Optional<Category> actual = categoryRepository.findById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo("a");
    }

    @Test
    void test2() {
        categoryService.addParentCategory("b");

        final Optional<Category> actual = categoryRepository.findById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo("b");
    }

    @Test
    void test3() {
        categoryService.addParentCategory("c");

        final Optional<Category> actual = categoryRepository.findById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo("c");
    }

    @Test
    void test4() {
        categoryService.addParentCategory("d");

        final Optional<Category> actual = categoryRepository.findById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo("d");
    }

    @Test
    void test5() {
        categoryService.addParentCategory("e");

        final Optional<Category> actual = categoryRepository.findById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo("e");
    }
}
