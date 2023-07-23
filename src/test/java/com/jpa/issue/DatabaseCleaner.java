package com.jpa.issue;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner {

    private final EntityManager em;
    private final List<String> tableNames;

    public DatabaseCleaner(EntityManager em) {
        this.em = em;
        this.tableNames = calculateTableNames();
    }

    private List<String> calculateTableNames() {
        return em.getMetamodel()
                .getEntities()
                .stream()
                .filter(entityType -> entityType.getJavaType()
                        .getAnnotation(Entity.class) != null)
                .map(entityType -> entityType.getName()
                        .toUpperCase())
                .toList();
    }

    @Transactional
    public void clean() {
        em.flush();
        em.createNativeQuery("SET foreign_key_checks = 0").executeUpdate();

        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            em.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }

        em.createNativeQuery("SET foreign_key_checks = 1").executeUpdate();
    }
}
