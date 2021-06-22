package io.swagger.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDTO {
    private BigDecimal amount;
    private String targetIban;
    private Date performDate;
    private String performingUser;
    private String performingIban;
}
