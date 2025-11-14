package org.example.orissem01.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.orissem01.exceptions.ConnectionException;
import org.example.orissem01.exceptions.MySQLException;
import org.example.orissem01.services.UserService;

import java.io.IOException;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(){
        ServletContext servletContext = getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.setAttribute("users", userService.getAll());
            request.getRequestDispatcher("/adminUsers.ftl").forward(request, response);
        } catch (MySQLException | ConnectionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
