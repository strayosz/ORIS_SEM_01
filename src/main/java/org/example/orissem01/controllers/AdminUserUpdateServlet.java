package org.example.orissem01.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.orissem01.exceptions.ConnectionException;
import org.example.orissem01.exceptions.MySQLException;
import org.example.orissem01.models.User;
import org.example.orissem01.services.UserService;

import java.io.IOException;

@WebServlet("/admin/user/update")
public class AdminUserUpdateServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(){
        ServletContext servletContext = getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String role = request.getParameter("selectedUserRole");
        User user = userService.findUserByLogin((String) session.getAttribute("selectedUserLogin"));
        try {
            userService.updateUserRole(user, role);
            request.getRequestDispatcher("/admin/users").forward(request, response);
        } catch (MySQLException | ConnectionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
