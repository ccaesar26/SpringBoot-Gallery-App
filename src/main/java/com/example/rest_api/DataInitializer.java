package com.example.rest_api;

import com.example.rest_api.users.database.model.*;
import com.example.rest_api.users.database.model.enums.Permission;
import com.example.rest_api.users.database.model.enums.Role;
import com.example.rest_api.users.service.PermissionService;
import com.example.rest_api.users.service.RoleService;
import com.example.rest_api.users.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public DataInitializer(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (var role : Role.values()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRole(role);

            if (!roleService.existsByName(roleEntity.getName())) {
                roleEntity = roleService.save(roleEntity);
            } else {
                roleEntity = roleService.findByNameAndAlbumId(roleEntity.getName(), null).get().getFirst();
            }

            for (Permission permission : Helper.rolePermissions.get(role)) {
                PermissionEntity permissionEntity = new PermissionEntity();
                permissionEntity.setName(permission.name());
                permissionEntity.setRole(roleEntity);
                permissionService.save(permissionEntity);
            }
        }

        var defaultRole = new RoleEntity();
        defaultRole.setRole(Role.DefaultRole);

        if (!roleService.existsByName(defaultRole.getName())) {
            defaultRole = roleService.save(defaultRole);
        } else if (!roleService.findByNameAndAlbumId(defaultRole.getName(), null).isEmpty()) {
            defaultRole = roleService.findByNameAndAlbumId(defaultRole.getName(), null).get().getFirst();
        }

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@admin.com");
        adminUser.setPassword("admin");
        adminUser.addRole(defaultRole);

        if (!userService.existsByEmail(adminUser.getEmail())) {
            this.userService.save(adminUser);
        }

        UserEntity viewerUser = new UserEntity();
        viewerUser.setUsername("user");
        viewerUser.setEmail("user@user.com");
        viewerUser.setPassword("user");
        viewerUser.addRole(defaultRole);

        if (!userService.existsByEmail(viewerUser.getEmail())) {
            this.userService.save(viewerUser);
        }
    }

}
