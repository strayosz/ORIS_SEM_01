package org.example.orissem01.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.orissem01.exceptions.NoSuchUserException;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.UserRepositoryImpl;
import org.example.orissem01.utils.Password;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserRepositoryImpl userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public void getAll(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        List<User> users = userRepository.getUsers();
        request.setAttribute("users", users);
    }

    public User findUserByLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        try {
            return userRepository.findUserByLogin(String.valueOf(session.getAttribute("userLogin")))
                    .orElseThrow(() -> new NoSuchUserException("Пользователя с таким логином не существует"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(HttpServletRequest request) throws SQLException, ClassNotFoundException {
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

        userRepository.addUser(user);
    }

    public String regUser(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        String resource = "/reg.ftl";

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        if (isEmpty(login) || isEmpty(password) || isEmpty(name) || isEmpty(surname)) {
            request.setAttribute("errormessage", "Введите корректные данные");
            return resource;
        } else {
            try {
                addUser(request);
            } catch (SQLException e) {
                request.setAttribute("errormessage", "Пользователь с таким логином уже существует");
                return resource;
            } catch (ClassNotFoundException e) {
                request.setAttribute("errormessage", "Что-то пошло не так, попробуйте еще раз...");
                return resource;
            }

            session.setAttribute("userLogin", login);
            return "/home.ftl";
        }
    }

    public String updateUser(HttpServletRequest request) {
        String resource = "/update.ftl";

        User user = findUserByLogin(request);

        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        if (isEmpty(password) || isEmpty(name) || isEmpty(surname)) {
            request.setAttribute("errormessage", "Введите корректные данные");
            request.setAttribute("user", user);
            return resource;
        }
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        user.setPassword(bCrypt.encode(password));
        user.setName(name);
        user.setSurname(surname);
        try {
            userRepository.updateUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("successmessage", "Данные успешно изменены!");
        request.setAttribute("user", user);
        return resource;

    }

    public String checkUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        String resource = "/home.ftl";

        if (session == null || session.getAttribute("userLogin") == null) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");

            if (isEmpty(login) || isEmpty(password)) {
                request.setAttribute("errormessage", "Логин и пароль не должны быть пустыми");
                resource = "/login.ftl";
            } else {
                try {
                    String userPassword = userRepository.getUserPasswordByLogin(login);
                    if (userPassword == null) {
                        request.setAttribute("errormessage", "Пользователя с таким логином не существует");
                        resource = "/login.ftl";
                    } else {
                        if (Password.matches(password, userPassword)) {
                            session = request.getSession(true);
                            session.setAttribute("userLogin", login);
                        } else {
                            request.setAttribute("errormessage", "Неверный пароль, попробуйте еще раз!");
                            resource = "/login.ftl";
                        }
                    }
                } catch (Exception e) {
                    request.setAttribute("errormessage", "Что-то пошло не так, попробуйте еще раз...");
                    resource = "/login.ftl";
                }
            }
        }

        return resource;
    }

    private boolean isEmpty(String paramName) {
        return paramName == null || paramName.isEmpty();
    }
}
