package com.manageinvoice.invoices;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Map; import java.util.UUID;
import org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; import org.springframework.boot.test.context.SpringBootTest; import org.springframework.http.MediaType; import org.springframework.test.context.ActiveProfiles; import org.springframework.test.web.servlet.MockMvc; import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class FinancialRecordWorkflowTests {
 @Autowired MockMvc mvc; @Autowired ObjectMapper mapper;
 @Test void createsInvoicesPaymentsAndRejectsOverpaymentAndCrossTenantAccess() throws Exception {
  String token=register("financial-phase6@example.com"); UUID workspace=createWorkspace(token,"Financial Phase 6"); UUID other=createWorkspace(register("financial-other@example.com"),"Other");
  String vendorBody=mapper.writeValueAsString(Map.of("name","Northstar Office","email","finance@example.com","phone","555-0100","address","1 Main Street"));
  MvcResult vendorResult=mvc.perform(post("/api/v1/vendors").header("Authorization","Bearer "+token).header("X-Workspace-Id",workspace).contentType(MediaType.APPLICATION_JSON).content(vendorBody)).andExpect(status().isOk()).andReturn(); UUID vendor=UUID.fromString(mapper.readTree(vendorResult.getResponse().getContentAsString()).get("id").asText());
  Map<String,Object> invoice=Map.of("vendorId",vendor,"invoiceNumber","INV-600","invoiceDate","2026-11-07","dueDate","2026-12-07","subtotal",100,"taxAmount",10,"totalAmount",110,"currency","USD"); String invoiceJson=mapper.writeValueAsString(invoice);
  MvcResult invoiceResult=mvc.perform(post("/api/v1/invoices").header("Authorization","Bearer "+token).header("X-Workspace-Id",workspace).contentType(MediaType.APPLICATION_JSON).content(invoiceJson)).andExpect(status().isOk()).andReturn(); UUID invoiceId=UUID.fromString(mapper.readTree(invoiceResult.getResponse().getContentAsString()).get("id").asText());
  Map<String,Object> partial=Map.of("paymentDate","2026-11-08","amount",40,"currency","USD","paymentMethod","BANK","referenceNumber","PAY-1");
  mvc.perform(post("/api/v1/invoices/{id}/payments",invoiceId).header("Authorization","Bearer "+token).header("X-Workspace-Id",workspace).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(partial))).andExpect(status().isOk());
  mvc.perform(get("/api/v1/invoices/{id}",invoiceId).header("Authorization","Bearer "+token).header("X-Workspace-Id",workspace)).andExpect(status().isOk()).andExpect(jsonPath("$.amountPaid").value(40)).andExpect(jsonPath("$.balanceDue").value(70)).andExpect(jsonPath("$.status").value("PARTIALLY_PAID"));
  mvc.perform(post("/api/v1/invoices/{id}/payments",invoiceId).header("Authorization","Bearer "+token).header("X-Workspace-Id",workspace).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(Map.of("paymentDate","2026-11-09","amount",71,"currency","USD")))).andExpect(status().isBadRequest());
  mvc.perform(post("/api/v1/invoices").header("Authorization","Bearer "+token).header("X-Workspace-Id",workspace).contentType(MediaType.APPLICATION_JSON).content(invoiceJson)).andExpect(status().isConflict());
  mvc.perform(get("/api/v1/invoices/{id}",invoiceId).header("Authorization","Bearer "+token).header("X-Workspace-Id",other)).andExpect(status().isForbidden());
 }
 private String register(String email)throws Exception{MvcResult r=mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(Map.of("email",email,"password","SecurePass123","firstName","Phase","lastName","Six")))).andExpect(status().isOk()).andReturn();return mapper.readTree(r.getResponse().getContentAsString()).get("accessToken").asText();}
 private UUID createWorkspace(String token,String name)throws Exception{MvcResult r=mvc.perform(post("/api/v1/workspaces").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(Map.of("name",name,"workspaceType","BUSINESS")))).andExpect(status().isOk()).andReturn();return UUID.fromString(mapper.readTree(r.getResponse().getContentAsString()).get("id").asText());}
}
