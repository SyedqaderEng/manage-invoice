package com.manageinvoice.invoices;
import static org.junit.jupiter.api.Assertions.assertEquals; import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal; import org.junit.jupiter.api.Test;
class InvoiceBalanceTests {
 private final InvoiceBalance balance=new InvoiceBalance();
 @Test void calculatesRemainingBalance(){ assertEquals(new BigDecimal("60.00"),balance.remaining(new BigDecimal("100.00"),new BigDecimal("40.00"))); }
 @Test void rejectsOverpayment(){ assertThrows(IllegalArgumentException.class,()->balance.validate(new BigDecimal("100.00"),new BigDecimal("60.00"),new BigDecimal("50.00"))); }
}
