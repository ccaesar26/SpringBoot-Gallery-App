package com.example.rest_api;

import com.example.rest_api.users.database.model.RoleEntity;
import com.example.rest_api.users.database.model.enums.Permission;
import com.example.rest_api.users.database.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.List;

public interface Helper {

    Map<Role, List<Permission>> rolePermissions = Map.of(
            Role.DefaultRole, List.of(
                    Permission.CreateResources,
                    Permission.ViewResourcesCover
            ),
            Role.ResourceOwner, List.of(
                    Permission.EditUsers,
                    Permission.EditRemoveFromResources,
                    Permission.EditAddToResources,
                    Permission.ViewResourcesContent
            ),
            Role.ResourceEditor, List.of(
                    Permission.EditRemoveFromResources,
                    Permission.EditAddToResources,
                    Permission.ViewResourcesContent
            ),
            Role.ResourceRestrictedEditor, List.of(
                    Permission.EditAddToResources,
                    Permission.ViewResourcesContent
            ),
            Role.ResourceViewer, List.of(
                    Permission.ViewResourcesContent,
                    Permission.ViewResourcesCover
            )
    );
    Map<Permission, List<String>> permissionUrls = Map.of(
            Permission.EditUsers, List.of("/album/*/manage-permissions"),
            Permission.CreateResources, List.of("/create-album"),
            Permission.EditRemoveFromResources, List.of("/album/*/deletePhoto"),
            Permission.EditAddToResources, List.of("/album/*/add-photo"),
            Permission.ViewResourcesContent, List.of("/album/*"),
            Permission.ViewResourcesCover, List.of("/home", "/images/**", "/home/search")
    );

    Map<String, String> possibleRolesWithPermissions = Map.of(
            "ResourceEditor", "Able to edit by both removing from and adding to a resource, Able to view the content of a resource",
            "ResourceRestrictedEditor", "Able to edit by only adding to a resource, Able to view the content of a resource",
            "ResourceViewer", "Able to view the content of a resource, Able to view only the cover of a resource",
            "DefaultRole", "Able to view only the cover of a resource"
    );

    Map<String, Role> roleMap = Map.of(
            "Default Role", Role.DefaultRole,
            "Owner of the resource", Role.ResourceOwner,
            "Editor", Role.ResourceEditor,
            "Restricted Editor", Role.ResourceRestrictedEditor,
            "Viewer", Role.ResourceViewer
    );

    Map<String, Permission> permissionMap = Map.of(
            "Able to manage access of a resource", Permission.EditUsers,
            "Able to create resources", Permission.CreateResources,
            "Able to edit by both removing from and adding to a resource", Permission.EditRemoveFromResources,
            "Able to edit by only adding to a resource", Permission.EditAddToResources,
            "Able to view the content of a resource", Permission.ViewResourcesContent,
            "Able to view only the cover of a resource", Permission.ViewResourcesCover
    );

    public static boolean hasPermission(
            List<RoleEntity> roles,
            Collection<? extends GrantedAuthority> grantedAuthorities,
            String email,
            Permission requiredPermission
    ) {
        for (var role : roles) {
            if (grantedAuthorities.stream().anyMatch(auth -> auth.getAuthority().equals(role.getName())) &&
                    role.getUsers().stream().anyMatch(user -> user.getEmail().equals(email))) {

                var permissions = Helper.rolePermissions.get(Role.valueOf(role.getName()));
                if (permissions.contains(requiredPermission)) {
                    return true;
                }
            }
        }
        return false;
    }

}
