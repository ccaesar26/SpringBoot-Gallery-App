package com.example.rest_api.controller;

import com.example.rest_api.users.service.AlbumService;
import com.example.rest_api.gallery.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/album")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    @Autowired
    public AlbumController(AlbumService albumService, PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
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

    // Manage access to an album
    @GetMapping("/manage-access/{id}")
    public String manageAccess(@PathVariable Long id, Model model) {
        var album = albumService.getAlbumById(id);
//        var sharedUsers = albumService.getSharedUsers(id); // Assuming this returns a list of users with access
        model.addAttribute("album", album);
//        model.addAttribute("sharedUsers", sharedUsers);
        return "manage-access"; // A new Thymeleaf template for access management
    }
}

