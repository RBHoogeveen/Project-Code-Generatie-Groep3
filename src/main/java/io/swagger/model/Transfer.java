package io.swagger.model;

import java.math.BigDecimal;

public interface Transfer {
    User getUserPerforming();

    BigDecimal getAmount();

    String getDate();
}
