package org.example.orissem01.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.orissem01.repositories.RecordRepositoryImpl;
import org.example.orissem01.repositories.TransactionRepositoryImpl;
import org.example.orissem01.repositories.UserRepositoryImpl;
import org.example.orissem01.repositories.mappers.EntityMapper;
import org.example.orissem01.services.RecordService;
import org.example.orissem01.services.TransactonService;
import org.example.orissem01.services.UserService;
import org.example.orissem01.utils.DBConnection;

@WebListener
public class ContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBConnection.init();

            EntityMapper entityMapper = new EntityMapper();

            UserRepositoryImpl userRepository = new UserRepositoryImpl(entityMapper);
            TransactionRepositoryImpl transactionRepository = new TransactionRepositoryImpl(entityMapper);
            RecordRepositoryImpl recordRepository = new RecordRepositoryImpl(entityMapper);

            UserService userService = new UserService(userRepository);
            RecordService recordService = new RecordService(recordRepository);
            TransactonService transactonService = new TransactonService(transactionRepository, userService, recordService);

            ServletContext context = sce.getServletContext();

            context.setAttribute("userService", userService);
            context.setAttribute("recordService", recordService);
            context.setAttribute("transactonService", transactonService);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        DBConnection.destroy();
    }
}
