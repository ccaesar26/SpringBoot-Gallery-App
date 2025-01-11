package com.example.rest_api.users.service;

import com.example.rest_api.Helper;
import com.example.rest_api.users.database.model.PermissionEntity;
import com.example.rest_api.users.database.model.enums.Permission;
import com.example.rest_api.users.database.model.enums.Role;
import com.example.rest_api.users.database.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public void save(PermissionEntity permissionEntity) {
        this.permissionRepository.save(permissionEntity);
    }

    public List<PermissionEntity> findAll() {
        return this.permissionRepository.findAll();
    }

    public List<PermissionEntity> findAllByRole(Role role) {
        var permissions = Helper.rolePermissions.get(role);
        return this.permissionRepository.findAll().stream()
                .filter(permission -> permissions.contains(Permission.valueOf(permission.getName())))
                .toList();
    }
}
