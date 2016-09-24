package me.songrgg.freshmeat.form.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author songrgg
 * @since 1.0
 */
@Documented
@Constraint(validatedBy = TextLengthValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TextLength {
  /**
   * The error message when validation failed.
   * @return the warning message when invalidated.
   */
  String message() default "TEXT_LENGTH_INCORRECT";

  Class<?>[] groups() default {};

  /**
   * The min word length of the given text.
   * @return the min word length.
   */
  int min() default 0;

  /**
   * The max word length of the given text.
   * @return the max word length.
   */
  int max() default Integer.MAX_VALUE;

  Class<? extends Payload>[] payload() default {};

}
