package com.example.rest_api.users.database.model;

import com.example.rest_api.users.database.model.enums.Role;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "roles")
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "album_id")
    private Long albumId;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<UserEntity> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "id"))
    private List<PermissionEntity> permissions = new ArrayList<>();

    @Override
    public String getAuthority() {
        return name;
    }

    public void setRole(Role role) {
        this.name = role.name();
    }
}