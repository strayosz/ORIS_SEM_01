package org.example.orissem01.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.orissem01.exceptions.ConnectionException;
import org.example.orissem01.exceptions.DublicateUserException;
import org.example.orissem01.services.UserService;

import java.io.IOException;

@WebServlet("/reg")
public class RegServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(){
        ServletContext servletContext = getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        request.getRequestDispatcher("/reg.ftl").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resource = "/reg.ftl";

        try {
            if (userService.regUser(request)) {
                HttpSession session = request.getSession(true);
                String login = request.getParameter("login");
                session.setAttribute("userLogin", login);
                resource = "/home.ftl";
            }
        } catch (ConnectionException e) {
            throw new RuntimeException(e.getMessage());
        } catch (DublicateUserException e) {
            request.setAttribute("errormessage", e.getMessage());
        }

        request.getRequestDispatcher(resource).forward(request, response);
    }
}
