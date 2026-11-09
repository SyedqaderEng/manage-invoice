package com.manageinvoice.vendors;
import java.util.List; import java.util.Locale; import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.manageinvoice.tenancy.WorkspaceService;
@Service public class VendorService {
 private final VendorRepository vendors; private final WorkspaceService workspaces;
 public VendorService(VendorRepository vendors,WorkspaceService workspaces){this.vendors=vendors;this.workspaces=workspaces;}
 public List<Vendor> list(UUID workspaceId,UUID userId){workspaces.requireMembership(workspaceId,userId);return vendors.findByWorkspaceId(workspaceId);}
 @Transactional public Vendor create(UUID workspaceId,UUID userId,CreateRequest request){workspaces.requireMembership(workspaceId,userId);String normalized=normalize(request.name());if(vendors.findByWorkspaceIdAndNormalizedName(workspaceId,normalized).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Vendor already exists in this workspace");Vendor vendor=new Vendor();vendor.setWorkspaceId(workspaceId);vendor.setNormalizedName(normalized);vendor.setDisplayName(request.name().trim());vendor.setEmail(request.email());vendor.setPhone(request.phone());vendor.setAddress(request.address());return vendors.save(vendor);}
 @Transactional public Vendor update(UUID workspaceId,UUID userId,UUID vendorId,CreateRequest request){workspaces.requireMembership(workspaceId,userId);Vendor vendor=vendors.findByWorkspaceIdAndId(workspaceId,vendorId).orElseThrow();String normalized=normalize(request.name());vendors.findByWorkspaceIdAndNormalizedName(workspaceId,normalized).filter(existing->!existing.getId().equals(vendorId)).ifPresent(existing->{throw new ResponseStatusException(HttpStatus.CONFLICT,"Vendor already exists in this workspace");});vendor.setNormalizedName(normalized);vendor.setDisplayName(request.name().trim());vendor.setEmail(request.email());vendor.setPhone(request.phone());vendor.setAddress(request.address());return vendors.save(vendor);}
 public void delete(UUID workspaceId,UUID userId,UUID vendorId){workspaces.requireMembership(workspaceId,userId);Vendor vendor=vendors.findByWorkspaceIdAndId(workspaceId,vendorId).orElseThrow();vendors.delete(vendor);}
 private String normalize(String name){return name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+"," ");}
 public record CreateRequest(String name,String email,String phone,String address){}
}
