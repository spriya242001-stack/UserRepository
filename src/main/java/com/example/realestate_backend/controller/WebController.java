package com.example.realestate_backend.controller;

import com.example.realestate_backend.entity.Property;
import com.example.realestate_backend.entity.PropertyType;
import com.example.realestate_backend.entity.User;
import com.example.realestate_backend.service.AdminService;
import com.example.realestate_backend.service.CloudinaryService;
import com.example.realestate_backend.service.PropertyService;
import com.example.realestate_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
public class WebController {
    private final PropertyService propertyService;
    private final AdminService adminService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    public WebController(PropertyService propertyService,
                         AdminService adminService,
                         UserService userService,
                         CloudinaryService cloudinaryService) {
        this.propertyService = propertyService;
        this.adminService = adminService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    // =========================
    // PUBLIC HOME / SEARCH PAGE
    // =========================

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) PropertyType type,
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute(
                "properties",
                propertyService.searchProperties(
                        location,
                        minPrice,
                        maxPrice,
                        type,
                        keyword
                )
        );

        return "index";
    }

    // =========================
    // PUBLIC PROPERTY DETAILS
    // =========================

    @GetMapping("/properties/{id}")
    public String propertyDetail(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "property",
                propertyService.getPropertyById(id)
        );

        return "property-detail";
    }

    // =========================
    // REGISTER
    // =========================

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result) {

        if (result.hasErrors()) {
            return "register";
        }

        userService.registerUser(user);

        return "redirect:/login?registered";
    }

    // =========================
    // LOGIN
    // =========================

    @GetMapping("/login")
    public String showLoginForm() {

        return "login";
    }

    // =========================
    // CUSTOMER DASHBOARD
    // =========================

    @GetMapping("/dashboard")
    public String customerDashboard(
            Principal principal,
            Model model) {

        User currentUser =
                userService.findByEmail(principal.getName());

        model.addAttribute(
                "properties",
                propertyService.getPropertiesByOwner(
                        currentUser.getId()
                )
        );

        model.addAttribute(
                "newProperty",
                new Property()
        );

        return "dashboard";
    }

    // =========================
    // CREATE PROPERTY
    // =========================

    @PostMapping("/properties/create")
    public String createProperty(
            @Valid @ModelAttribute("newProperty") Property property,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            Principal principal,
            Model model) throws IOException {

        User currentUser =
                userService.findByEmail(principal.getName());

        if (result.hasErrors()) {

            model.addAttribute(
                    "properties",
                    propertyService.getPropertiesByOwner(
                            currentUser.getId()
                    )
            );

            return "dashboard";
        }

        if (imageFile != null && !imageFile.isEmpty()) {

            String imageUrl =
                    cloudinaryService.uploadImage(imageFile);

            property.setImageUrl(imageUrl);
        }

        propertyService.createProperty(
                property,
                currentUser
        );

        return "redirect:/dashboard?created";
    }
    @GetMapping("/properties/create")
    public String showCreatePropertyForm() {
        return "redirect:/dashboard";
    }

    // =========================
    // ADMIN DASHBOARD
    // =========================

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {

        model.addAttribute(
                "users",
                adminService.getAllUsers()
        );

        model.addAttribute(
                "pendingProperties",
                adminService.getPendingProperties()
        );

        return "admin";
    }

    // =========================
    // APPROVE PROPERTY
    // =========================

    @PostMapping("/admin/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveProperty(
            @PathVariable Long id) {

        adminService.approveProperty(id);

        return "redirect:/admin?approved";
    }

    // =========================
    // DELETE USER
    // =========================

    @PostMapping("/admin/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(
            @PathVariable Long id) {

        adminService.deleteUser(id);

        return "redirect:/admin?userDeleted";
    }
}

