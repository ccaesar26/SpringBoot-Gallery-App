package com.example.rest_api.controller;

import com.example.rest_api.Helper;
import com.example.rest_api.gallery.service.AlbumService;
import com.example.rest_api.gallery.service.PhotoService;
import com.example.rest_api.users.database.model.PermissionEntity;
import com.example.rest_api.users.database.model.RoleEntity;
import com.example.rest_api.users.database.model.UserEntity;
import com.example.rest_api.users.database.model.enums.Permission;
import com.example.rest_api.users.database.model.enums.Role;
import com.example.rest_api.users.service.PermissionService;
import com.example.rest_api.users.service.RoleService;
import com.example.rest_api.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/album")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public AlbumController(AlbumService albumService, PhotoService photoService, UserService userService, RoleService roleService, PermissionService permissionService) {
        this.albumService = albumService;
        this.photoService = photoService;
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    // View a specific album by ID
    @GetMapping("/{id}")
    public String viewAlbum(@PathVariable Long id, Model model) {
        var album = albumService.getAlbumById(id);
        var photos = photoService.getPhotosByAlbumId(id);
        model.addAttribute("album", album);
        model.addAttribute("photos", photos);
        return "album"; // Thymeleaf template for the album detail view
    }

    @GetMapping("/{id}/add-photo")
    public String addPhotoForm(@PathVariable Long id, Model model) {
        var album = albumService.getAlbumById(id);
        model.addAttribute("album", album);
        return "add-photo"; // Thymeleaf template for the form to add a photo
    }

    // Add a photo to the album
    @PostMapping("/{id}/add-photo")
    public String addPhotoToAlbum(
            @PathVariable Long id,
            @RequestParam("photos") MultipartFile[] photos,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var album = albumService.getAlbumById(id);
            var photoList = Arrays.asList(photos);
            photoService.savePhotosForAlbum(photoList, album);
            redirectAttributes.addFlashAttribute("successMessage", "Photo added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add photo: " + e.getMessage());
        }
        return "redirect:/album/" + id;
    }

    // Delete a photo
    @PostMapping("/{id}/deletePhoto")
    public String deletePhoto(@PathVariable Long id, @RequestParam("photoId") Long photoId, RedirectAttributes redirectAttributes) {
        try {
            photoService.deletePhoto(photoId);
            redirectAttributes.addFlashAttribute("message", "Photo deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        }
        return "redirect:/album/" + id;
    }

    @GetMapping("/{id}/manage-permissions")
    public String managePermissions(@PathVariable String id, Model model) {
        var album = albumService.getAlbumById(Long.parseLong(id));
        var owner = userService.findById(album.getOwnerId());
        var users = userService.findAll().stream().filter(
                user -> !user.getId().equals(owner.getId()) && (
                        user.getRoles().stream().anyMatch(role -> album.getId().equals(role.getAlbumId()))
                                || user.getRoles().stream().anyMatch(
                                role -> role.getAlbumId() == null && role.getName().equals("DefaultRole")
                        )
                )
        ).toList();

        var userRoleMap = new HashMap<UserEntity, RoleEntity>();
        for (var user : users) {
            var roles = userService.findByIdWithRoles(user.getId()).getRoles();
            roles = roles.stream().filter(
                    role -> role.getAlbumId() != null && role.getAlbumId().equals(album.getId())
            ).toList();
            var role = user.getRoles().stream().filter(
                    r -> r.getAlbumId() != null && r.getAlbumId().equals(album.getId())
            ).findFirst().orElseGet(() -> roleService.findByNameAndAlbumId("DefaultRole", null).get().getFirst());
            userRoleMap.put(user, role);
        }

        var possibleRoles = Helper.possibleRolesWithPermissions;

        model.addAttribute("album", album); // AlbumEntity
        model.addAttribute("owner", owner); // UserEntity
        model.addAttribute("userRoleMap", userRoleMap); // Map<UserEntity, RoleEntity>
        model.addAttribute("possibleRoles", possibleRoles); // Map<String, List<String>>

        return "manage-permissions";
    }

    @Transactional
    @PostMapping("/save-role")
    public String updatePermissions(
            @RequestParam("userId") Long userId,
            @RequestParam("albumId") Long albumId,
            @RequestParam("role") String role
    ) {
        var user = userService.findById(userId);
        var roleEntity = roleService.findByNameAlbumIdAndUserId(role, albumId, userId).or(
                () -> {
                    var newRole = new RoleEntity();
                    newRole.setName(role);
                    newRole.setAlbumId(albumId);
                    newRole = roleService.save(newRole);
                    return java.util.Optional.ofNullable(roleService.save(newRole));
                }
        ).get();

        user.getRoles().removeIf(r -> r.getAlbumId() != null && r.getAlbumId().equals(albumId));
        user.getRoles().add(roleEntity);
        userService.save(user);

        return "redirect:/album/" + albumId + "/manage-permissions";
    }
}

