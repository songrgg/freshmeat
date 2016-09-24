package me.songrgg.freshmeat.form.validator;

import me.songrgg.freshmeat.helper.HtmlParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @author songrgg
 * @since 1.0
 */
public class TextLengthValidator implements ConstraintValidator<TextLength, String> {
  private Integer min;

  private Integer max;

  @Override
  public void initialize(TextLength paramA) {
    this.min = paramA.min();
    this.max = paramA.max();
  }

  @Override
  public boolean isValid(String content, ConstraintValidatorContext ctx) {
    if (content == null) {
      return false;
    }
    HtmlParser parser = new HtmlParser();
    parser.setContent(content);

    String pureText = parser.text();
    return pureText.length() >= min && pureText.length() < max;
  }
}
