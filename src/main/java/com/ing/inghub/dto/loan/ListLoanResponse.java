package com.ing.inghub.dto.loan;

import com.ing.inghub.dto.base.BaseResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ListLoanResponse extends BaseResponse {
    private List<LoanDto> loans;
}
