package org.example.orissem01.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.orissem01.exceptions.ConnectionException;
import org.example.orissem01.exceptions.MySQLException;
import org.example.orissem01.models.Record;
import org.example.orissem01.models.User;
import org.example.orissem01.services.RecordService;
import org.example.orissem01.services.TransactonService;
import org.example.orissem01.services.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private UserService userService;
    private RecordService recordService;
    private TransactonService transactonService;

    @Override
    public void init(){
        ServletContext servletContext = getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
        this.recordService = (RecordService) servletContext.getAttribute("recordService");
        this.transactonService = (TransactonService) servletContext.getAttribute("transactonService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = userService.findUserByLogin(request);

        List<Record> records;
        try {
            records = recordService.getExchangedRecords(user);
        } catch (ConnectionException | MySQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        if (records.isEmpty()) {
            request.setAttribute("isEmptyExchanged", "Нет доступных для обмена смен");
        }

        request.setAttribute("records", records);
        request.getRequestDispatcher("/home.ftl").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getParameter("choosedRecordId") != null) {
            transactonService.addTransaction(request);
        }
        doGet(request, response);
    }
}
