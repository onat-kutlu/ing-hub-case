package com.ing.inghub.constants.constants;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {
    public static final Long DEFAULT_CREDIT_LIMIT = 100000L;
    public static final List<Integer> AVAILABLE_INSTALLMENT_COUNT_LUST = List.of(6, 9, 12, 24);
    public static final Double INTEREST_RATE_UPPER_LIMIT = 0.5;
    public static final Double INTEREST_RATE_LOWER_LIMIT = 0.1;
}
