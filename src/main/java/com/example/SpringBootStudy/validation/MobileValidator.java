package com.example.SpringBootStudy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileValidator implements ConstraintValidator<MobileCheck, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return MobileValidator.isValidMobile(value);
    }

    private static String mobilePattern = "^[0-9]{8,11}";

    public static boolean isValidMobile(String target) {
        Matcher matcher = Pattern.compile(mobilePattern).matcher(target);
        return matcher.matches();
    }

}
