package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

// Import các class Business và Data (Bạn cần phải tạo 2 file này)
import model.User;
import data.UserDB;

// Định tuyến URL cho Servlet
@WebServlet(urlPatterns = {"/emailList"})
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        // Giá trị URL mặc định
        String url = "/index.jsp";
        
        // Biến để chứa thông báo lỗi hoặc thành công
        String message = "";

        // 1. Lấy hành động (action) từ form
        String action = request.getParameter("action");
        if (action == null) {
            action = "join";  // hành động mặc định
        }

        // 2. Xử lý hành động
        if (action.equals("join")) {
            url = "/index.jsp";    // Chuyển về trang nhập form
        } 
        else if (action.equals("add")) {
            // Lấy dữ liệu từ form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // Tạo đối tượng User (chứa dữ liệu)
            User user = new User(firstName, lastName, email);

            // Kiểm tra xem email đã tồn tại trong DB chưa (Sử dụng class UserDB)
            if (UserDB.emailExists(user.getEmail())) {
                message = "This email address already exists.<br>" +
                          "Please enter another email address.";
                url = "/index.jsp"; // Quay lại trang nhập để sửa lỗi
            } else {
                message = "";
                url = "/thanks.jsp"; // Chuyển sang trang cảm ơn
                UserDB.insert(user); // Lưu user vào Database
            }
            
            // Lưu đối tượng user và thông báo vào request để hiển thị bên JSP
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        // 3. Chuyển hướng trang (Forward)
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
    }
    
    // Thêm hàm doGet để xử lý khi người dùng truy cập trực tiếp bằng đường link
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}