package com.lszymik;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RandomTests {

  public static final String DEBIT_PAYMENT = "DEBIT";
  public static final String CREDIT_PAYMENT = "CREDIT";
  public static final String CASH_PAYMENT = "CASH";
  public static final String CREATED = "CREATED";
  public static final String IN_CHECKOUT = "IN_CHECKOUT";

  @Test
  void optionalTestForExplanation() {
    log.info("Example how reproduce a bug in code.");

    final Optional<String> paymentType = Optional.of(DEBIT_PAYMENT);

    final String status =
        paymentType.filter(isUpfrontPayment())
            .map(__ -> IN_CHECKOUT)
            .orElse(CREATED);
  }

  private Predicate<String> isUpfrontPayment() {
    return p -> p.equals(DEBIT_PAYMENT) || p.equals(CREDIT_PAYMENT);
  }

  @Test
  void optionalEquals() {
    final Optional<String> o1 = Optional.empty();
    final Optional<String> o2 = Optional.empty();

    log.info("Equals? {}", o1.equals(o2));
  }

  @Test
  void bigDecimalTest() {
    final BigDecimal decimal1 = BigDecimal.valueOf(190, 3);
    log.info("Big decimal: " + decimal1.toString());

    final BigDecimal decimal2 = decimal1.setScale(6, RoundingMode.HALF_EVEN);
    log.info("Big decimal2: " + decimal2.toString());
  }

}
