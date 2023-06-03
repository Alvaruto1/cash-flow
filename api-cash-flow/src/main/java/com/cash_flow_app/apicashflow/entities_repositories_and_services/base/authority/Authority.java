package com.cash_flow_app.apicashflow.entities_repositories_and_services.base.authority;

import com.cash_flow_app.apicashflow.utils.PermissionName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="authorities")
public class Authority {

    public Authority(String endpoint, PermissionName permission) {
        this.permission = permission;
        this.endpoint = endpoint;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PermissionName permission;

    private String endpoint;
}
