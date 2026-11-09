package com.manageinvoice.expenses;
import java.math.BigDecimal; import java.time.LocalDate; import java.util.UUID;
import org.springframework.stereotype.Service; import com.fasterxml.jackson.databind.JsonNode; import com.fasterxml.jackson.databind.ObjectMapper;
@Service public class DocumentExpenseCreationService {
 private final ExpenseRepository expenses; private final ObjectMapper mapper;
 public DocumentExpenseCreationService(ExpenseRepository expenses,ObjectMapper mapper){this.expenses=expenses;this.mapper=mapper;}
 public void createReceipt(UUID workspaceId,UUID userId,UUID documentId,String correctedJson){
  try {JsonNode data=mapper.readTree(correctedJson);if(expenses.findByWorkspaceIdAndDocumentId(workspaceId,documentId).isPresent())return;BigDecimal total=new BigDecimal(text(data,"total","0").replaceAll("[^0-9.-]",""));ExpenseTransaction expense=new ExpenseTransaction();expense.setWorkspaceId(workspaceId);expense.setDocumentId(documentId);expense.setDescription(text(data,"vendorName",null));expense.setTransactionDate(parseDate(text(data,"documentDate",null)));expense.setAmount(total);expense.setTaxAmount(new BigDecimal(text(data,"tax","0")));expense.setCurrency(text(data,"currency","USD"));expense.setPaymentStatus("PAID");expense.setExpenseStatus("APPROVED");expense.setSourceType("DOCUMENT");expense.setCreatedBy(userId);expenses.save(expense);} catch(Exception exception){throw new IllegalStateException("Unable to create expense from approved document",exception);}
 }
 private String text(JsonNode data,String field,String fallback){JsonNode value=data.get(field);return value==null||value.isNull()?fallback:value.asText(fallback);}
 private LocalDate parseDate(String value){try{return LocalDate.parse(value);}catch(Exception exception){return LocalDate.now();}}
}
