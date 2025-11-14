package org.example.orissem01.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.orissem01.exceptions.ConnectionException;
import org.example.orissem01.exceptions.MySQLException;
import org.example.orissem01.models.Transaction;
import org.example.orissem01.services.TransactonService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/transactions")
public class AdminTransactionsServlet extends HttpServlet {

    private TransactonService transactonService;

    @Override
    public void init(){
        ServletContext servletContext = getServletContext();
        this.transactonService = (TransactonService) servletContext.getAttribute("transactonService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Transaction> transactions = null;
        try {
            transactions = transactonService.getAll();
            if (!transactions.isEmpty()) {
                request.setAttribute("transactions", transactions);
            }
            request.getRequestDispatcher("/adminTransactions.ftl").forward(request, response);
        } catch (MySQLException | ConnectionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
