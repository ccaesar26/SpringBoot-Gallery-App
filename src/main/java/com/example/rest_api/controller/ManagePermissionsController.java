package com.example.rest_api.controller;

import com.example.rest_api.users.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage-permissions")
public class ManagePermissionsController {
    private final PermissionService permissionService;

    @Autowired
    public ManagePermissionsController(
            PermissionService permissionService
    ) {
        this.permissionService = permissionService;
    }

    @GetMapping("/{id}")
    public String managePermissions() {
        return "manage-permissions";
    }
}
