package com.example.rest_api.controller;

import com.example.rest_api.DataInitializer;
import com.example.rest_api.users.database.model.AlbumEntity;
import com.example.rest_api.users.service.AlbumService;
import com.example.rest_api.gallery.service.PhotoService;
import com.example.rest_api.users.database.model.PermissionEntity;
import com.example.rest_api.users.database.model.enums.Role;
import com.example.rest_api.users.database.model.RoleEntity;
import com.example.rest_api.users.service.PermissionService;
import com.example.rest_api.users.service.RoleService;
import com.example.rest_api.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;

@Controller
@RequestMapping("/create-album")
public class CreateAlbumController {
    private final AlbumService albumService;
    private final PhotoService photoService;
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public CreateAlbumController(AlbumService albumService, PhotoService photoService, UserService userRepository, RoleService roleService, PermissionService permissionService) {
        this.albumService = albumService;
        this.photoService = photoService;
        this.userService = userRepository;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping()
    public String createAlbumForm(Model model, Principal principal) {
        return "create-album";
    }

    @PostMapping()
    public String createAlbum(
            @RequestParam("title") String title,
            @RequestParam("photos") MultipartFile[] photos,
            Principal principal
    ) {
        // Get the owner of the album (current user)
        var email = principal.getName();
        var owner = userService.findByEmail(email);

        // Create the album
        var album = new AlbumEntity();
        album.setTitle(title);
        album.setOwnerId(owner.getId());

        // Save the album to the database
        var savedAlbum = albumService.saveAlbum(album);

        // Grant the owner all permissions to the album
        var ownerRole = new RoleEntity();
        ownerRole.setRole(Role.ResourceOwner);
        ownerRole.setAlbumId(savedAlbum.getId());
        roleService.save(ownerRole);

        if (!roleService.existsByName(Role.ResourceOwner.name())) {
            for (var permission : DataInitializer.rolePermissions.get(Role.ResourceOwner)) {
                var permissionEntity = new PermissionEntity();
                permissionEntity.setName(permission.name());
                permissionEntity.setRole(ownerRole);
                permissionService.save(permissionEntity);
            }
        }

        // Save the uploaded photos
        var photoList = Arrays.asList(photos);
        photoService.savePhotosForAlbum(photoList, savedAlbum);

        // Redirect to the home page
        return "redirect:/home";
    }
}
