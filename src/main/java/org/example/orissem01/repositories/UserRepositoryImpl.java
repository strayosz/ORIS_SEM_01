package org.example.orissem01.repositories;

import org.example.orissem01.models.Record;
import org.example.orissem01.models.Slot;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.interfaces.IUserRepository;
import org.example.orissem01.repositories.mappers.EntityMapper;
import org.example.orissem01.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements IUserRepository {

    private final EntityMapper entityMapper;

    public UserRepositoryImpl(EntityMapper entityMapper){
        this.entityMapper = entityMapper;
    }

    @Override
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<User> users = new ArrayList<>();
        String sql = """
                        select account_id a_account_id, login a_login, password a_password,
                               name a_name, surname a_surname, role a_role
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

    @Override
    public Optional<User> findUserByLogin(String login) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        String sql = """
            select account_id a_account_id, login a_login, password a_password,
                   name a_name, surname a_surname, role a_role
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

    @Override
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

    @Override
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

    @Override
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

        statement.close();
        connection.commit();
        connection.close();
    }

    @Override
    public void deleteUserByLogin(String login) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        String sql = """
            delete from accounts
            where login = ?;
            """;

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.executeUpdate();

        statement.close();
        connection.commit();
        connection.close();
    }

    @Override
    public Record mapRecord(ResultSet resultSet) throws SQLException {
        Record record = entityMapper.mapRecordDefault(resultSet);
        Slot slot = entityMapper.mapSlotDefault(resultSet);
        record.setSlot(slot);
        return record;
    }

    @Override
    public User mapUser(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        User user = entityMapper.mapUserDefault(resultSet);
        user.setRecords(getRecordsByUserId(user.getId()));
        return user;
    }

    @Override
    public List<Record> getRecordsByUserId(Long userId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<Record> records = new ArrayList<>();
        String sql = """
            select r.account_slot_id r_account_slot_id, r.chats_count r_chats_count,
                   r.status r_status, r.comment r_comment,
                   s.slot_id s_slot_id, s.name s_name, s.date s_date, s.time s_time,
                   s.type s_type
            from slots s
            join account_slot acs on s.slot_id = acs.slot_id
            join records r on acs.account_slot_id = r.account_slot_id
            where account_id = ?
            order by date, time;
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, userId);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            records.add(mapRecord(resultSet));
        }

        statement.close();
        resultSet.close();
        connection.close();

        return records;
    }
}