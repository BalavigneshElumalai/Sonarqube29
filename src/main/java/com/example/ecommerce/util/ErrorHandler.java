package com.example.ecommerce.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ErrorHandler {
    
    // Duplicate error handling - security hotspot
    public static void handleValidationError(RedirectAttributes redirectAttributes, String field, String message) {
        redirectAttributes.addFlashAttribute("errorMessage", field + ": " + message);
        redirectAttributes.addFlashAttribute("errorField", field);
    }
    
    // Duplicate error handling - security hotspot (same logic as above)
    public static void addValidationError(RedirectAttributes redirectAttributes, String fieldName, String errorMessage) {
        redirectAttributes.addFlashAttribute("errorMessage", fieldName + ": " + errorMessage);
        redirectAttributes.addFlashAttribute("errorField", fieldName);
    }
    
    // Duplicate success message handling - security hotspot
    public static void handleSuccessMessage(RedirectAttributes redirectAttributes, String action, String entity) {
        redirectAttributes.addFlashAttribute("successMessage", entity + " " + action + " successfully!");
    }
    
    // Duplicate success message handling - security hotspot (same logic as above)
    public static void addSuccessMessage(RedirectAttributes redirectAttributes, String operation, String item) {
        redirectAttributes.addFlashAttribute("successMessage", item + " " + operation + " successfully!");
    }
    
    // Duplicate exception handling - security hotspot
    public static String handleDatabaseException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Database error occurred: " + e.getMessage());
        return "redirect:/web/products";
    }
    
    // Duplicate exception handling - security hotspot (same logic as above)
    public static String processDatabaseError(Exception exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Database error occurred: " + exception.getMessage());
        return "redirect:/web/products";
    }
}
