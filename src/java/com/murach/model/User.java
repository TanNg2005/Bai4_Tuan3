package com.murach.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * User model class for Email List Application
 * Compatible with Jakarta EE 11
 */
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Constants for validation
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 100;
    
    // User properties
    private String firstName;
    private String lastName;
    private String email;
    private String dob;                // Date of Birth (YYYY-MM-DD format)
    private String source;             // How did you hear about us?
    private String offers;             // Nhận thông tin CDs/offers
    private String emailAnnouncements; // Nhận email announcements
    private String contactMethod;      // Contact method (Email/Postal)
    private LocalDateTime createdAt;   // Timestamp when user was created
    
    // Constructor mặc định
    public User() {
        this.createdAt = LocalDateTime.now();
        this.offers = "no";
        this.emailAnnouncements = "no";
        this.contactMethod = "emailOrMail";
    }
    
    // Constructor đầy đủ
    public User(String firstName, String lastName, String email,
                String dob, String source, String offers,
                String emailAnnouncements, String contactMethod) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.source = source;
        this.offers = offers != null ? offers : "no";
        this.emailAnnouncements = emailAnnouncements != null ? emailAnnouncements : "no";
        this.contactMethod = contactMethod != null ? contactMethod : "emailOrMail";
    }
    
    // Getters và Setters với validation
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = sanitizeString(firstName);
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = sanitizeString(lastName);
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = sanitizeString(email);
    }
    
    public String getDob() {
        return dob;
    }
    
    public void setDob(String dob) {
        this.dob = sanitizeString(dob);
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = sanitizeString(source);
    }
    
    public String getOffers() {
        return offers;
    }
    
    public void setOffers(String offers) {
        this.offers = normalizeYesNo(offers);
    }
    
    public String getEmailAnnouncements() {
        return emailAnnouncements;
    }
    
    public void setEmailAnnouncements(String emailAnnouncements) {
        this.emailAnnouncements = normalizeYesNo(emailAnnouncements);
    }
    
    public String getContactMethod() {
        return contactMethod;
    }
    
    public void setContactMethod(String contactMethod) {
        this.contactMethod = sanitizeString(contactMethod);
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Utility methods
    
    /**
     * Lấy tên đầy đủ
     * @return Full name
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName.trim());
        }
        return fullName.toString();
    }
    
    /**
     * Kiểm tra user có muốn nhận offers không
     * @return true nếu muốn nhận offers
     */
    public boolean wantsOffers() {
        return "yes".equalsIgnoreCase(offers);
    }
    
    /**
     * Kiểm tra user có muốn nhận email announcements không
     * @return true nếu muốn nhận email announcements
     */
    public boolean wantsEmailAnnouncements() {
        return "yes".equalsIgnoreCase(emailAnnouncements);
    }
    
    /**
     * Lấy tuổi từ date of birth
     * @return tuổi hoặc -1 nếu không tính được
     */
    public int getAge() {
        if (dob == null || dob.trim().isEmpty()) {
            return -1;
        }
        
        try {
            LocalDate birthDate = LocalDate.parse(dob.trim());
            LocalDate currentDate = LocalDate.now();
            return currentDate.getYear() - birthDate.getYear() - 
                   (currentDate.getDayOfYear() < birthDate.getDayOfYear() ? 1 : 0);
        } catch (DateTimeParseException e) {
            return -1;
        }
    }
    
    /**
     * Lấy formatted created date
     * @return formatted date string
     */
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return createdAt.format(formatter);
    }
    
    // Validation methods
    
    /**
     * Kiểm tra user có hợp lệ không
     * @return true nếu user hợp lệ
     */
    public boolean isValid() {
        return isValidName(firstName) && 
               isValidName(lastName) && 
               isValidEmail(email);
    }
    
    /**
     * Kiểm tra tên có hợp lệ không
     * @param name tên cần kiểm tra
     * @return true nếu hợp lệ
     */
    public static boolean isValidName(String name) {
        return name != null && 
               !name.trim().isEmpty() && 
               name.trim().length() <= MAX_NAME_LENGTH &&
               name.matches("^[a-zA-ZÀ-ỹ\\s]+$"); // Chỉ cho phép chữ cái và khoảng trắng
    }
    
    /**
     * Kiểm tra email có hợp lệ không
     * @param email email cần kiểm tra
     * @return true nếu hợp lệ
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty() || email.length() > MAX_EMAIL_LENGTH) {
            return false;
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.trim().matches(emailRegex);
    }
    
    /**
     * Kiểm tra date of birth có hợp lệ không
     * @param dob date string
     * @return true nếu hợp lệ
     */
    public static boolean isValidDob(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            return true; // Optional field
        }
        
        try {
            LocalDate birthDate = LocalDate.parse(dob.trim());
            LocalDate currentDate = LocalDate.now();
            LocalDate minDate = currentDate.minusYears(120); // Max 120 years old
            
            return !birthDate.isAfter(currentDate) && !birthDate.isBefore(minDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    // Private helper methods
    
    /**
     * Làm sạch string input
     * @param input input string
     * @return cleaned string
     */
    private String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("\\s+", " "); // Normalize whitespace
    }
    
    /**
     * Normalize yes/no values
     * @param value input value
     * @return "yes" or "no"
     */
    private String normalizeYesNo(String value) {
        if (value == null) {
            return "no";
        }
        return "yes".equalsIgnoreCase(value.trim()) ? "yes" : "no";
    }
    
    // Object methods
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return Objects.equals(email, user.email); // Email is unique identifier
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", source='" + source + '\'' +
                ", offers='" + offers + '\'' +
                ", emailAnnouncements='" + emailAnnouncements + '\'' +
                ", contactMethod='" + contactMethod + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
    
    /**
     * Tạo string representation an toàn cho logging
     * @return safe string for logging
     */
    public String toSafeString() {
        return "User{" +
                "firstName='" + (firstName != null ? firstName.substring(0, Math.min(firstName.length(), 3)) + "***" : "null") + '\'' +
                ", lastName='" + (lastName != null ? lastName.substring(0, Math.min(lastName.length(), 3)) + "***" : "null") + '\'' +
                ", email='" + (email != null ? email.replaceAll("(.{2}).*@", "$1***@") : "null") + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}