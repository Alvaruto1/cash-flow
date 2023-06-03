package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority;

import com.cash_flow_app.apicashflow.utils.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByPermissionAndEndpoint(PermissionName permissionName, String endpoint);
    List<Authority> findAllByEndpoint(String endpoint);
}
