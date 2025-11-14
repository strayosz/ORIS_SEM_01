package org.example.orissem01.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.orissem01.exceptions.*;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.UserRepositoryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserRepositoryImpl userRepository;

    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() throws MySQLException, ConnectionException {
        try {
            return userRepository.getAll();
        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
    }

    public User findUserByLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        try {
            return userRepository.findUserByLogin(String.valueOf(session.getAttribute("userLogin")))
                    .orElseThrow(() -> new NoSuchUserException());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserByLogin(String login) {
        try {
            return userRepository.findUserByLogin(login)
                    .orElseThrow(() -> new NoSuchUserException());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean regUser(HttpServletRequest request) throws ConnectionException, DublicateUserException {
        try {
            User user = mapUser(request);
            userRepository.addUser(user);
        } catch (SQLException e) {
            throw new DublicateUserException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
        return true;
    }

    public void updateUser(HttpServletRequest request) throws MySQLException, ConnectionException {
        User user = findUserByLogin(request);
        try {
            mapUpdateUser(request, user);
            userRepository.updateUser(user);
        } catch (NotValidPassword e) {
            request.setAttribute("errormessage", e.getMessage());
        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        } finally {
            request.setAttribute("user", user);
        }
    }

    public void updateUserRole(User user, String role) throws MySQLException, ConnectionException {
        user.setRole(role);
        try {
            userRepository.updateUser(user);
        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
    }

    public String getUserHashPassword(String login) throws ConnectionException, MySQLException {
        try {
            return userRepository.getUserPasswordByLogin(login);
        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
    }



    public void deleteUser(String login) throws MySQLException, ConnectionException {
        try {
            userRepository.deleteUserByLogin(login);
        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
    }

    private User mapUser(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setSurname(surname);
        user.setRole("Стажёр");

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        user.setPassword(bCrypt.encode(password));

        return user;
    }

    private void mapUpdateUser(HttpServletRequest request, User user) throws NotValidPassword {

        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        if (!bCrypt.matches(oldPassword, user.getPassword())) {
            throw new NotValidPassword("Неверный старый пароль");
        }
        user.setPassword(bCrypt.encode(newPassword));
        user.setName(name);
        user.setSurname(surname);
    }
}
