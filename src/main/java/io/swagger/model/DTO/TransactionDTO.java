package io.swagger.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {
    private BigDecimal amount;
    private String performerIban;
    private String targetIban;
}
