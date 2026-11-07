package com.manageinvoice.invoices;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
@Component public class InvoiceBalance {
 public BigDecimal remaining(BigDecimal total, BigDecimal paid){return total.subtract(paid);}
 public void validate(BigDecimal total, BigDecimal paid, BigDecimal newPayment){if(newPayment.signum()<0||paid.add(newPayment).compareTo(total)>0)throw new IllegalArgumentException("Payment exceeds invoice total");}
}
