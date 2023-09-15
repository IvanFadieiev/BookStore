package project.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.PropertyUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch,
        Object> {
    private String initialField;
    private String fieldForComparingInitialField;

    public void initialize(final FieldMatch constraintAnnotation) {
        initialField = constraintAnnotation.first();
        fieldForComparingInitialField = constraintAnnotation.second();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = PropertyUtils
                    .getProperty(value, this.initialField);
            final Object secondObj = PropertyUtils
                    .getProperty(value, this.fieldForComparingInitialField);
            return firstObj == null && secondObj == null
                    || firstObj != null
                    && firstObj.equals(secondObj);
        } catch (final Exception ex) {
            return false;
        }
    }
}
