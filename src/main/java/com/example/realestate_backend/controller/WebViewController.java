package com.example.realestate_backend.controller;

import com.example.realestate_backend.entity.Property;
import com.example.realestate_backend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebViewController {

    @Autowired
    private PropertyService propertyService;

    // 1. Home Page -> index.html
    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "index";
    }

    // 2. All Properties Page (with filter support) -> properties.html
    @GetMapping("/properties")
    public String showPropertiesPage(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            Model model) {

        List<Property> properties;
        if ((location != null && !location.isBlank()) || (type != null && !type.isBlank())) {
            properties = propertyService.searchProperties(location, type);
        } else {
            properties = propertyService.getAllProperties();
        }

        model.addAttribute("properties", properties);
        return "properties";
    }

    // 3. Property Details Page -> property-details.html
    @GetMapping("/properties/{id}")
    public String showPropertyDetails(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        model.addAttribute("property", property);
        return "property-details";
    }

    // 4. Add Property Form Page -> add-property.html
    @GetMapping("/add-property")
    public String showAddPropertyPage(Model model) {
        model.addAttribute("property", new Property());
        return "add-property";
    }

    // 5. Login Page -> login.html
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}