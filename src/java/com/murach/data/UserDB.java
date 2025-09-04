package com.murach.data;

import com.murach.model.User;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserDB {
    
    private static final Logger logger = Logger.getLogger(UserDB.class.getName());
    private static final String DEFAULT_FILE_NAME = "users.txt";
    private static final String SEPARATOR = "-------------------------------";
    
    /**
     * Phương thức để thêm user vào file
     * Sử dụng try-with-resources và NIO.2 để đảm bảo tài nguyên được giải phóng
     * @param user User object cần lưu
     * @return 1 nếu thành công, 0 nếu thất bại
     */
    public static int insert(User user) {
        if (user == null) {
            logger.warning("Cannot insert null user");
            return 0;
        }
        
        try {
            // Lấy đường dẫn file an toàn hơn
            String filePath = getFilePath();
            Path path = Paths.get(filePath);
            
            // Tạo thư mục nếu chưa tồn tại
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            // Sử dụng try-with-resources để đảm bảo tài nguyên được đóng
            try (BufferedWriter writer = Files.newBufferedWriter(path, 
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                
                // Thêm timestamp
                String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                writer.write("Timestamp: " + timestamp);
                writer.newLine();
                
                // Ghi đầy đủ thông tin user với null safety
                writer.write("Email: " + sanitizeOutput(user.getEmail()));
                writer.newLine();
                writer.write("First Name: " + sanitizeOutput(user.getFirstName()));
                writer.newLine();
                writer.write("Last Name: " + sanitizeOutput(user.getLastName()));
                writer.newLine();
                writer.write("Date of Birth: " + sanitizeOutput(user.getDob()));
                writer.newLine();
                writer.write("Heard About Us: " + sanitizeOutput(user.getSource()));
                writer.newLine();
                writer.write("Wants Offers: " + sanitizeOutput(user.getOffers()));
                writer.newLine();
                writer.write("Email Announcements: " + sanitizeOutput(user.getEmailAnnouncements()));
                writer.newLine();
                writer.write("Contact Method: " + sanitizeOutput(user.getContactMethod()));
                writer.newLine();
                writer.write(SEPARATOR);
                writer.newLine();
                writer.newLine();
                
                logger.info("User inserted successfully: " + sanitizeLog(user.getEmail()));
                return 1; // Thành công
            }
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error inserting user to file", e);
            return 0; // Thất bại
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error inserting user", e);
            return 0;
        }
    }
    
    /**
     * Kiểm tra email đã tồn tại trong file chưa
     * @param email email cần kiểm tra
     * @return true nếu email đã tồn tại
     */
    public static boolean emailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            String filePath = getFilePath();
            Path path = Paths.get(filePath);
            
            if (!Files.exists(path)) {
                return false;
            }
            
            List<String> lines = Files.readAllLines(path);
            String targetEmail = "Email: " + email.trim().toLowerCase();
            
            return lines.stream()
                .map(String::toLowerCase)
                .anyMatch(line -> line.equals(targetEmail));
                
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error checking email existence", e);
            return false;
        }
    }
    
    /**
     * Lấy tất cả users từ file
     * @return danh sách users
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        try {
            String filePath = getFilePath();
            Path path = Paths.get(filePath);
            
            if (!Files.exists(path)) {
                logger.info("User file does not exist: " + filePath);
                return users;
            }
            
            List<String> lines = Files.readAllLines(path);
            User currentUser = null;
            
            for (String line : lines) {
                line = line.trim();
                
                if (line.isEmpty() || line.equals(SEPARATOR)) {
                    if (currentUser != null) {
                        users.add(currentUser);
                        currentUser = null;
                    }
                    continue;
                }
                
                if (line.startsWith("Timestamp:")) {
                    currentUser = new User();
                    continue;
                }
                
                if (currentUser != null) {
                    parseUserProperty(currentUser, line);
                }
            }
            
            // Thêm user cuối cùng nếu có
            if (currentUser != null) {
                users.add(currentUser);
            }
            
            logger.info("Retrieved " + users.size() + " users from file");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading users from file", e);
        }
        
        return users;
    }
    
    /**
     * Xóa tất cả users (clear file)
     * @return true nếu thành công
     */
    public static boolean clearAllUsers() {
        try {
            String filePath = getFilePath();
            Path path = Paths.get(filePath);
            
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("User file cleared successfully");
            }
            
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error clearing user file", e);
            return false;
        }
    }
    
    /**
     * Lấy đường dẫn file an toàn
     */
    private static String getFilePath() {
        // Sử dụng system property hoặc temp directory
        String userHome = System.getProperty("user.home");
        String dataDir = System.getProperty("app.data.dir", userHome + File.separator + ".emaillist");
        return dataDir + File.separator + DEFAULT_FILE_NAME;
    }
    
    /**
     * Làm sạch output để tránh injection
     */
    private static String sanitizeOutput(String input) {
        if (input == null) {
            return "";
        }
        // Loại bỏ các ký tự có thể gây vấn đề
        return input.replaceAll("[\\r\\n\\t]", " ").trim();
    }
    
    /**
     * Làm sạch log để tránh log injection
     */
    private static String sanitizeLog(String input) {
        if (input == null) {
            return "null";
        }
        return input.replaceAll("[\\r\\n\\t]", "_").trim();
    }
    
    /**
     * Parse thuộc tính user từ dòng text
     */
    private static void parseUserProperty(User user, String line) {
        if (line.startsWith("Email: ")) {
            user.setEmail(line.substring(7));
        } else if (line.startsWith("First Name: ")) {
            user.setFirstName(line.substring(12));
        } else if (line.startsWith("Last Name: ")) {
            user.setLastName(line.substring(11));
        } else if (line.startsWith("Date of Birth: ")) {
            user.setDob(line.substring(15));
        } else if (line.startsWith("Heard About Us: ")) {
            user.setSource(line.substring(16));
        } else if (line.startsWith("Wants Offers: ")) {
            user.setOffers(line.substring(14));
        } else if (line.startsWith("Email Announcements: ")) {
            user.setEmailAnnouncements(line.substring(21));
        } else if (line.startsWith("Contact Method: ")) {
            user.setContactMethod(line.substring(16));
        }
    }
}