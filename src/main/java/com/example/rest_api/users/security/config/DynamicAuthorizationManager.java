package com.example.rest_api.users.security.config;

import com.example.rest_api.DataInitializer;
import com.example.rest_api.users.database.model.enums.Role;
import com.example.rest_api.users.database.model.RoleEntity;
import com.example.rest_api.users.service.RoleService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final RoleService roleService;
    private final AntPathMatcher pathMatcher;

    public DynamicAuthorizationManager(RoleService roleService) {
        this.roleService = roleService;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        var requestUrl = context.getRequest().getRequestURI();

        var grantedAuthority = authentication.get().getAuthorities().stream().findFirst().isPresent()
                ? authentication.get().getAuthorities().stream().findFirst().get()
                : null;

        if (Objects.isNull(grantedAuthority)) {
            return new AuthorizationDecision(false);
        }

        var albumId = extractAlbumId(requestUrl);

        List<RoleEntity> roles = roleService.findAll();

        var actualRole = roles.stream()
                .filter(role -> role.getName().equals(grantedAuthority.getAuthority()))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(actualRole)) {
            return new AuthorizationDecision(false);
        }

        if (authentication.get().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(actualRole.getName()))) {

            var hasPermission = false;


            var permissions = DataInitializer.rolePermissions.get(Role.valueOf(actualRole.getName()));
            for (var permission : permissions) {
                for (var url : DataInitializer.permissionUrls.get(permission)) {
                    if (pathMatcher.match(url, requestUrl)) {
                        hasPermission = true;
                        break;
                    }
                }
            }

            if (hasPermission) {
                return new AuthorizationDecision(true);
            }

        }
        return new AuthorizationDecision(false);
    }

    // Method to extract album ID from URL
    private Integer extractAlbumId(String requestUrl) {
        // Using regular expression to match an integer immediately after "/album/"
        Pattern pattern = Pattern.compile("/album/(\\d+)");
        Matcher matcher = pattern.matcher(requestUrl);

        if (matcher.find()) {
            // Return the matched album ID as integer
            return Integer.parseInt(matcher.group(1));
        }

        return null; // Return null if no match is found
    }
}
