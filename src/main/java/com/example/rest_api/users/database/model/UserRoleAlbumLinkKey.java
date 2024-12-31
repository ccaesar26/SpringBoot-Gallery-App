package com.example.rest_api.users.database.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Embeddable
public class UserRoleAlbumLinkKey implements Serializable {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "album_id")
    private AlbumEntity album;
}
