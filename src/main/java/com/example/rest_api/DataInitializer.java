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

import java.util.Map;
import java.util.Set;

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
    public void run(ApplicationArguments args) {
        var defaultRole = new RoleEntity();
        defaultRole.setRole(Role.DefaultRole);

        if (!roleService.existsByName(defaultRole.getName())) {
            roleService.save(defaultRole);
            for (Permission permission : rolePermissions.get(Role.DefaultRole)) {
                PermissionEntity permissionEntity = new PermissionEntity();
                permissionEntity.setName(permission.name());
                permissionEntity.setRole(defaultRole);
                permissionService.save(permissionEntity);
            }

        } else if (roleService.findByNameAndAlbumId(defaultRole.getName(), null).isPresent()) {
            defaultRole = roleService.findByNameAndAlbumId(defaultRole.getName(), null).get();
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

    public static final Map<Role, Set<Permission>> rolePermissions = Map.of(
            Role.DefaultRole, Set.of(
                    Permission.CreateResources,
                    Permission.ViewResourcesCover
            ),
            Role.ResourceOwner, Set.of(
                    Permission.EditUsers,
                    Permission.EditRemoveFromResources,
                    Permission.EditAddToResources,
                    Permission.ViewResourcesContent
            ),
            Role.ResourceEditor, Set.of(
                    Permission.EditRemoveFromResources,
                    Permission.EditAddToResources,
                    Permission.ViewResourcesContent
            ),
            Role.ResourceRestrictedEditor, Set.of(
                    Permission.EditAddToResources,
                    Permission.ViewResourcesContent
            ),
            Role.ResourceViewer, Set.of(
                    Permission.ViewResourcesContent,
                    Permission.ViewResourcesCover
            )
    );

    public static final Map<Permission, Set<String>> permissionUrls = Map.of(
            Permission.EditUsers, Set.of("/album/*/manage-permissions/**"),
            Permission.CreateResources, Set.of("/create-album"),
            Permission.EditRemoveFromResources, Set.of("/album/*/deletePhoto"),
            Permission.EditAddToResources, Set.of("/album/*/add-photos"),
            Permission.ViewResourcesContent, Set.of("/album/*"),
            Permission.ViewResourcesCover, Set.of("/home", "/images/**")
    );
}
