package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority;

import com.cash_flow_app.apicashflow.utils.PermissionName;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    public List<Authority> createAuthorityEndpoint(@NonNull String endpoint){
        return authorityRepository.saveAll(List.of(
                new Authority(endpoint, PermissionName.READ),
                new Authority(endpoint, PermissionName.CREATE),
                new Authority(endpoint, PermissionName.UPDATE),
                new Authority(endpoint, PermissionName.DELETE)
        ));
    }

    public List<Authority> getAllPermissionsToEndpoint(@NonNull String endpoint){
        return authorityRepository.findAllByEndpoint(endpoint);
    }

    public Optional<Authority> findByPermissionAndEndpoint(PermissionName permissionName, String endpoint){
        return authorityRepository.findByPermissionAndEndpoint(permissionName, endpoint);
    }

    public long count(){
        return authorityRepository.count();
    }


}
