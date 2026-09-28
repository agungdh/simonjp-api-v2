package id.my.agungdh.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.EntityManager;

public interface SoftDeletableRepository<T, ID> extends PanacheRepositoryBase<T, ID> {

    default void enableDeletedFilter() {
        getEntityManager().unwrap(org.hibernate.Session.class)
                .enableFilter("deletedFilter")
                .setParameter("deleted", true);
    }

    default void disableDeletedFilter() {
        getEntityManager().unwrap(org.hibernate.Session.class)
                .disableFilter("deletedFilter");
    }
}
