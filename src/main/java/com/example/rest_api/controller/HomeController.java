package com.example.rest_api.controller;

import com.example.rest_api.gallery.dto.AlbumWithUsernameDto;
import com.example.rest_api.users.service.AlbumService;
import com.example.rest_api.users.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/home")
public class HomeController {
    UserRepository userRepository;
    private final AlbumService albumService;

    @Autowired
    public HomeController(
            UserRepository userRepository,
            AlbumService albumService
    ) {
        this.userRepository = userRepository;
        this.albumService = albumService;
    }

    @GetMapping()
    public String home(Model model, Principal principal) {
        String email = principal.getName();
        var owner = userRepository.findByEmail(email).orElseThrow();
        var myAlbums = albumService.getAlbumsByOwner(owner.getId());
        var otherAlbums = albumService.getAllAlbums().stream()
                .filter(album -> !album.getOwnerId().equals(owner.getId()))
                .map(album -> {
                    var user = userRepository.findById(album.getOwnerId()).orElseThrow();
                    return new AlbumWithUsernameDto(album, user.getUsername());
                })
                .toList();

        model.addAttribute("query", "");
        model.addAttribute("owner", owner);
        model.addAttribute("myAlbums", myAlbums);
        model.addAttribute("otherAlbums", otherAlbums);
        return "home";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute String query, Model model) {
        model.addAttribute("searchResults", albumService.searchAlbums(query));
        return "home";
    }
}

