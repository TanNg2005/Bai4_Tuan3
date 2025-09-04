package com.murach.controller;

import com.murach.data.UserDB;
import com.murach.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Year;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {
        
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int year = Year.now().getValue();
        request.setAttribute("year", year);
        
        // Set encoding để hỗ trợ UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "join"; 
        }
        
        String url = "/index.jsp"; // default
        
        if (action.equals("join")) {
            url = "/join.jsp";   // quay lại form đăng ký
        }
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set encoding để hỗ trợ UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }
        
        String url = "/index.jsp";
        
        if (action.equals("add")) {
            // Lấy dữ liệu từ form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String dob = request.getParameter("dob");
            String source = request.getParameter("source");
            String offers = request.getParameter("offers"); 
            String emailAnnouncements = request.getParameter("emailAnnouncements"); 
            String contactMethod = request.getParameter("contactMethod");
            
            // Tạo User object
            User user = new User();
            user.setFirstName(firstName != null ? firstName.trim() : "");
            user.setLastName(lastName != null ? lastName.trim() : "");
            user.setEmail(email != null ? email.trim() : "");
            user.setDob(dob != null ? dob.trim() : "");
            user.setSource(source != null ? source : "");
            user.setOffers(offers != null ? "yes" : "no");
            user.setEmailAnnouncements(emailAnnouncements != null ? "yes" : "no");
            user.setContactMethod(contactMethod != null ? contactMethod : "");
            
            // Validate dữ liệu
            String message = "";
            if (firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
                
                message = "Vui lòng nhập đầy đủ thông tin.";
                url = "/join.jsp";
                
            } else if (!isValidEmail(email.trim())) {
                message = "Email không hợp lệ. Vui lòng nhập lại.";
                url = "/join.jsp";
                
            } else {
                try {
                    // Lưu user vào database (nếu cần)
                    int result = UserDB.insert(user);
                    if (result > 0) {
                        request.setAttribute("user", user);
                        url = "/thanks.jsp";
                    } else {
                        message = "Có lỗi xảy ra khi lưu dữ liệu. Vui lòng thử lại.";
                        url = "/join.jsp";
                    }
                } catch (Exception e) {
                    // Log error for debugging
                    getServletContext().log("Error inserting user: " + e.getMessage(), e);
                    message = "Có lỗi xảy ra. Vui lòng thử lại.";
                    url = "/join.jsp";
                }
            }
            
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
    
    /**
     * Kiểm tra email có hợp lệ không
     * @param email email cần kiểm tra
     * @return true nếu email hợp lệ
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Simple email validation using regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}