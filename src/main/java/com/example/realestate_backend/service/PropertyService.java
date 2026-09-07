package com.example.realestate_backend.service;

import com.example.realestate_backend.dto.PropertyRequest;
import com.example.realestate_backend.entity.Property;
import com.example.realestate_backend.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    // 1. Fetch all properties
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // 2. Fetch property by ID
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

    // 3. Save a new property from Entity
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    // 4. Save a new property from DTO
    public Property savePropertyFromRequest(PropertyRequest request) {
        Property property = new Property();
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setLocation(request.getLocation());
        property.setPropertyType(request.getPropertyType());
        property.setListingType(request.getListingType());
        property.setIsAvailable(request.getIsAvailable());
        return propertyRepository.save(property);
    }

    // 5. Update an existing property
    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = getPropertyById(id);
        if (property != null) {
            property.setTitle(propertyDetails.getTitle());
            property.setDescription(propertyDetails.getDescription());
            property.setPrice(propertyDetails.getPrice());
            property.setLocation(propertyDetails.getLocation());
            property.setPropertyType(propertyDetails.getPropertyType());
            property.setListingType(propertyDetails.getListingType());
            property.setIsAvailable(propertyDetails.getIsAvailable());
            return propertyRepository.save(property);
        }
        return null;
    }

    // 6. Delete a property by ID
    public boolean deleteProperty(Long id) {
        if (propertyRepository.existsById(id)) {
            propertyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 7. Search properties by location and type
    public List<Property> searchProperties(String location, String type) {
        return propertyRepository.searchProperties(location, type);
    }
}