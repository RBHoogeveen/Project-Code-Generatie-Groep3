package io.swagger.model.DTO;

import io.swagger.model.TransferType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DepositWithdrawalDTO {
    private BigDecimal amount;
}
