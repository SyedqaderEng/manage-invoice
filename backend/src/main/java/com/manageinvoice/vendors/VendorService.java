package com.manageinvoice.vendors;
import java.util.List; import java.util.Locale; import java.util.UUID;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import com.manageinvoice.tenancy.WorkspaceService;
@Service public class VendorService {
 private final VendorRepository vendors; private final WorkspaceService workspaces;
 public VendorService(VendorRepository vendors,WorkspaceService workspaces){this.vendors=vendors;this.workspaces=workspaces;}
 public List<Vendor> list(UUID workspaceId,UUID userId){workspaces.requireMembership(workspaceId,userId);return vendors.findByWorkspaceId(workspaceId);}
 @Transactional public Vendor create(UUID workspaceId,UUID userId,CreateRequest request){workspaces.requireMembership(workspaceId,userId);String normalized=request.name().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+"," ");Vendor vendor=new Vendor();vendor.setWorkspaceId(workspaceId);vendor.setNormalizedName(normalized);vendor.setDisplayName(request.name().trim());vendor.setEmail(request.email());vendor.setPhone(request.phone());vendor.setAddress(request.address());return vendors.save(vendor);}
 public record CreateRequest(String name,String email,String phone,String address){}
}
