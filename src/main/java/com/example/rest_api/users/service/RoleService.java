package com.example.rest_api.users.service;

import com.example.rest_api.users.database.model.RoleEntity;
import com.example.rest_api.users.database.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(RoleEntity role) {
        this.roleRepository.save(role);
    }

    public Boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Optional<RoleEntity> findByNameAndAlbumId(String name, Long albumId) {
        return roleRepository.findByNameAndAlbumId(name, albumId);
    }

    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }
}
