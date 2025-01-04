package com.example.rest_api.users.service;

import com.example.rest_api.users.database.model.PermissionEntity;
import com.example.rest_api.users.database.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
