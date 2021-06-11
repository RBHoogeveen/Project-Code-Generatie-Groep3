package io.swagger.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDTO {
    private BigDecimal amount;
    private String targetIban;
    private String performDate;
    private String performingUser;
    private String performingIban;
}
