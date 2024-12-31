package com.example.rest_api.users.database.model;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Transient;

public class UserRoleAlbumLink {

    @EmbeddedId
    private final UserRoleAlbumLinkKey userRoleAlbumLinkKey = new UserRoleAlbumLinkKey();

    @Transient
    private UserEntity getUser() {
        return userRoleAlbumLinkKey.getUser();
    }

    @Transient
    private RoleEntity getRole() {
        return userRoleAlbumLinkKey.getRole();
    }

    @Transient
    private AlbumEntity getAlbum() {
        return userRoleAlbumLinkKey.getAlbum();
    }
}

