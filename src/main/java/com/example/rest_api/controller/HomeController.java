package com.example.rest_api.controller;

import com.example.rest_api.gallery.dto.AlbumWithUsernameDto;
import com.example.rest_api.gallery.service.AlbumService;
import com.example.rest_api.users.database.model.enums.Permission;
import com.example.rest_api.users.database.repository.UserRepository;
import com.example.rest_api.users.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final RoleService roleService;
    UserRepository userRepository;
    private final AlbumService albumService;

    @Autowired
    public HomeController(
            UserRepository userRepository,
            AlbumService albumService,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.albumService = albumService;
        this.roleService = roleService;
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
                    var dto = new AlbumWithUsernameDto(album, user.getUsername());

                    // Check if the user has permission to access the album
                    var roles = roleService.findAllByAlbumId(album.getId());
                    var grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

                    boolean hasPermission = roles.stream().anyMatch(role ->
                            grantedAuthorities.stream().anyMatch(auth ->
                                    auth.getAuthority().equals(role.getName()) &&
                                            role.getUsers().stream().anyMatch(u -> u.getEmail().equals(email))
                            )
                    );

                    dto.setCanAccess(hasPermission);
                    return dto;
                })
                .toList();

        /*
        * otherAlbums is a list of AlbumWithUsernameDto objects.
        * AlbumWithUsernameDto has three fields: album, username and canAccess (boolean, default is false).
        * You need to set the canAccess field to true if the user has the permission to access the album.
        * */

        model.addAttribute("query", "");
        model.addAttribute("owner", owner);
        model.addAttribute("myAlbums", myAlbums);
        model.addAttribute("otherAlbums", otherAlbums);
        return "home";
    }

//    @PostMapping("/search")
//    public String search(@ModelAttribute String query, Model model) {
//        model.addAttribute("searchResults", albumService.searchAlbums(query));
//        return "home";
//    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model, Principal principal) {
        String email = principal.getName();
        var owner = userRepository.findByEmail(email).orElseThrow();
        var searchResults = new ArrayList<AlbumWithUsernameDto>();

        for (var album : albumService.searchAlbums(query)) {
            var user = userRepository.findById(album.getOwnerId()).orElseThrow();
            var dto = new AlbumWithUsernameDto(album, user.getUsername());

            // Check if the user has permission to access the album
            var roles = roleService.findAllByAlbumId(album.getId());
            var grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            boolean hasPermission = roles.stream().anyMatch(role ->
                    grantedAuthorities.stream().anyMatch(auth ->
                            auth.getAuthority().equals(role.getName()) &&
                                    role.getUsers().stream().anyMatch(u -> u.getEmail().equals(email))
                    )
            );

            dto.setCanAccess(hasPermission);
            searchResults.add(dto);
        }

        model.addAttribute("query", query);
        model.addAttribute("searchResults", searchResults);
        return "search"; // Update this to your search results template name
    }

}

