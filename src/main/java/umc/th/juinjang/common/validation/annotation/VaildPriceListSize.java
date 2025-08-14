package umc.th.juinjang.common.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import umc.th.juinjang.common.validation.validator.PriceListVaildation;

@Documented
@Constraint(validatedBy = PriceListVaildation.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface VaildPriceListSize{

  int minSize() default 1; // 최소 허용 개수
  int maxSize() default 2; // 최대 허용 개수
  String message() default "전달받은 가격 배열의 길이가 0이거나 3이상, 혹은 null입니다. 다시 확인해주세요.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
