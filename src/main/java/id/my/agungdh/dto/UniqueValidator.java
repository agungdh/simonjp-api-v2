package id.my.agungdh.dto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@ApplicationScoped
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    EntityManager entityManager;

    private String entity;
    private String field;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.entity = constraintAnnotation.entity();
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Long count = (Long) entityManager.createQuery(
                "SELECT COUNT(*) FROM " + entity + " e WHERE e." + field + " = :value AND e.deletedAt IS NULL"
        ).setParameter("value", value).getSingleResult();

        return count == 0;
    }
}
