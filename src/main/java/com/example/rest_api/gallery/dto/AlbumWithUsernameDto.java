package com.example.rest_api.gallery.dto;

import com.example.rest_api.gallery.database.model.AlbumEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlbumWithUsernameDto {
    private AlbumEntity album;
    private String username;

    public AlbumWithUsernameDto(AlbumEntity album, String username) {
        this.album = album;
        this.username = username;
    }
}
