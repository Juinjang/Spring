package umc.th.juinjang.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.th.juinjang.common.validation.annotation.VaildPriceListSize;

@Component
@RequiredArgsConstructor
public class PriceListVaildation implements ConstraintValidator<VaildPriceListSize, List<String>> {

private int minSize;
private int maxSize;

@Override
public void initialize(VaildPriceListSize constraintAnnotation) {
    this.minSize = constraintAnnotation.minSize();
    this.maxSize = constraintAnnotation.maxSize();
    }

@Override
public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    if (value == null) {
    return false;
    }
    int priceCount = value.size();
    return priceCount >= minSize && priceCount <= maxSize;
    }

}
