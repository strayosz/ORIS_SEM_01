package org.example.orissem01.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.orissem01.services.RecordService;

import java.io.IOException;

@WebServlet("/user/records")
public class UserSlotsServlet extends HttpServlet {
    private final RecordService recordService = new RecordService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resource = recordService.getUserRecords(request);
        request.getRequestDispatcher(resource).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resource = recordService.updateUserRecord(request);
        recordService.getUserRecords(request);
        request.getRequestDispatcher(resource).forward(request, response);
    }
}
