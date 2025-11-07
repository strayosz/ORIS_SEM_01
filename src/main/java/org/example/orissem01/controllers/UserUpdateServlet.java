package org.example.orissem01.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.orissem01.services.UserService;

import java.io.IOException;

@WebServlet("/user/update")
public class UserUpdateServlet extends HomeServlet{

    private final UserService userService = new UserService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("user", userService.findUserByLogin(request));
        request.getRequestDispatcher("/userUpdate.ftl").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resource = userService.updateUser(request);
        request.getRequestDispatcher(resource).forward(request, response);
    }
}
