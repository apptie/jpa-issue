package com.jpa.issue;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DatabaseCleanListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        final EntityManager em = findEntityManager(testContext);
        final List<String> tableNames = calculateTableNames(em);

        clean(em, tableNames);
    }

    private EntityManager findEntityManager(TestContext testContext) {
        return testContext.getApplicationContext()
                .getBean(EntityManager.class);
    }

    private List<String> calculateTableNames(EntityManager em) {
        return em.getMetamodel()
                .getEntities()
                .stream()
                .filter(entityType -> entityType.getJavaType()
                        .getAnnotation(Entity.class) != null)
                .map(entityType -> entityType.getName()
                        .toUpperCase())
                .toList();
    }

    private void clean(EntityManager em, List<String> tableNames) {
        em.flush();
        em.createNativeQuery("SET foreign_key_checks = 0").executeUpdate();

        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            em.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }

        em.createNativeQuery("SET foreign_key_checks = 1").executeUpdate();
    }
}
