package org.example.orissem01.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.orissem01.exceptions.ConnectionException;
import org.example.orissem01.exceptions.MySQLException;
import org.example.orissem01.models.Record;
import org.example.orissem01.models.Slot;
import org.example.orissem01.models.Transaction;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.TransactionRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class TransactonService {
    private final TransactionRepositoryImpl transactionRepository;
    private final UserService userService;
    private final RecordService recordService;

    public TransactonService(TransactionRepositoryImpl transactionRepository, UserService userService, RecordService recordService){
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.recordService = recordService;
    }

    public List<Transaction> getAll() throws MySQLException, ConnectionException {
        try {
            return transactionRepository.getTransactions();
        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
    }

    public void addTransaction(HttpServletRequest request){
        Transaction transaction = mapTransaction(request);
        try {
            transactionRepository.addTransaction(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Transaction mapTransaction(HttpServletRequest request) {
        Transaction transaction = new Transaction();

        Long recordId = Long.parseLong(request.getParameter("choosedRecordId"));
        Record record = recordService.findRecordById(recordId);
        User fromUser = record.getUser();
        Slot slot = record.getSlot();

        User toUser = userService.findUserByLogin(request);

        String comment = request.getParameter("comment");

        transaction.setFromUser(fromUser);
        transaction.setToUser(toUser);
        transaction.setSlot(slot);
        transaction.setComment(comment);

        return transaction;
    }

}
