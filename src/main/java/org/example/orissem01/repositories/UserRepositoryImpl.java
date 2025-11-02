package org.example.orissem01.repositories;

import org.example.orissem01.models.User;
import org.example.orissem01.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl {

    public List<User> getUsers() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<User> users = new ArrayList<>();
        String sql = """
                        select account_id, login, password, name, surname, role
                        from accounts
                        """;
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            users.add(mapUser(resultSet));
        }

        statement.close();
        resultSet.close();
        connection.close();

        return users;
    }

    public Optional<User> findUserByLogin(String login) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        String sql = """
            select account_id, login, password, name, surname, role
            from accounts
            where login = ?
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();

        User user = null;
        if(resultSet.next()){
            user = mapUser(resultSet);
        }

        statement.close();
        resultSet.close();
        connection.close();

        return Optional.ofNullable(user);
    }

    public void addUser(User user) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        //Вытаскиваем айди
        String sqlSelectId = "select id from nextval('account_id_seq') as id";
        PreparedStatement statement = connection.prepareStatement(sqlSelectId);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()){
            Long id = resultSet.getLong("id");
            user.setId(id);
        }

        resultSet.close();
        statement.close();


        //Вставляем пользователя
        String sqlInsert = """
            insert into accounts(account_id, login, password, name, surname, role) values (?, ?, ?, ?, ?, ?);
            """;

        statement = connection.prepareStatement(sqlInsert);
        statement.setLong  (1, user.getId());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getName());
        statement.setString(5, user.getSurname());
        statement.setString(6, user.getRole());
        statement.executeUpdate();

        statement.close();
        connection.commit();
        connection.close();
    }

    public String getUserPasswordByLogin(String login) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        String password = null;
        String sql = "select password from accounts where login = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()){
            password = resultSet.getString("password");
        }

        resultSet.close();
        statement.close();
        connection.close();

        return password;
    }

    public void updateUser(User user) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        String sql = """
            update accounts
            set login = ?, password = ?, name = ?, surname = ?, role = ?
            where account_id = ?;
            """;

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getName());
        statement.setString(4, user.getSurname());
        statement.setString(5, user.getRole());
        statement.setLong  (6, user.getId());
        statement.executeUpdate();

        connection.commit();
        connection.close();
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId      (resultSet.getLong  ("account_id"));
        user.setLogin   (resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setName    (resultSet.getString("name"));
        user.setSurname (resultSet.getString("surname"));
        user.setRole    (resultSet.getString("role"));
        return user;
    }
}
