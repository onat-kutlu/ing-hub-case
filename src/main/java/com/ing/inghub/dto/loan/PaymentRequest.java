package com.ing.inghub.dto.loan;

import com.ing.inghub.dto.base.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PaymentRequest extends BaseRequest {
    @NotNull
    private Long loanId;
    @Positive
    private Long paymentAmount;
}
