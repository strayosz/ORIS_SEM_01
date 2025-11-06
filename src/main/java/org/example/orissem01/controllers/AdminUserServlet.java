package org.example.orissem01.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.orissem01.services.UserService;

import java.io.IOException;

@WebServlet("/admin/user")
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String login = request.getParameter("selectedUserLogin");
        session.setAttribute("selectedUserLogin", login);
        request.setAttribute("selectedUser", userService.findUserByLogin(login));
        request.getRequestDispatcher("/adminUser.ftl").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String resource = userService.updateUserRole(request);
        request.getRequestDispatcher(resource).forward(request, response);
    }

}
