package project.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.PropertyUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch,
        Object> {
    private String firstFieldName;
    private String secondFieldName;

    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = PropertyUtils
                    .getProperty(value, this.firstFieldName);
            final Object secondObj = PropertyUtils
                    .getProperty(value, this.secondFieldName);
            return firstObj == null && secondObj == null
                    || firstObj != null
                    && firstObj.equals(secondObj);
        } catch (final Exception ex) {
            return false;
        }
    }
}
