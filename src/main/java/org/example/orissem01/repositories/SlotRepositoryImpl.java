package org.example.orissem01.repositories;

import org.example.orissem01.models.Record;
import org.example.orissem01.models.Slot;
import org.example.orissem01.models.User;
import org.example.orissem01.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SlotRepositoryImpl {

    public void addSlot(Slot slot) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        //Вытаскиваем айди
        String sqlSelectId = "select id from nextval('slot_id_seq') as id";
        PreparedStatement statement = connection.prepareStatement(sqlSelectId);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()){
            Long id = resultSet.getLong("id");
            slot.setId(id);
        }

        resultSet.close();
        statement.close();

        //Вставляем слот
        String sqlInsert = """
            insert into slots(slot_id, name, date, time, type) values (?, ?, ?, ?, ?);
            """;

        statement = connection.prepareStatement(sqlInsert);
        statement.setLong   (1, slot.getId());
        statement.setString (2, slot.getName());
        statement.setString (3, slot.getDate());
        statement.setString (4, slot.getTime());
        statement.setString (5, slot.getType());
        statement.executeUpdate();

        statement.close();
        connection.commit();
        connection.close();
    }

    public Optional<Slot> findSlotByDate(String date, String time) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        String sql = """
            select slot_id, name, date, time, type
            from slots
            where date = ? AND time = ?
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, date);
        statement.setString(1, time);
        ResultSet resultSet = statement.executeQuery();

        Slot slot = null;
        if(resultSet.next()){
            slot = mapSlot(resultSet);
        }

        statement.close();
        resultSet.close();
        connection.close();

        return Optional.ofNullable(slot);
    }

    public Optional<Slot> findSlotById(Long id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        String sql = """
            select slot_id, name, date, time, type
            from slots
            where slot_id = ?
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();

        Slot slot = null;
        if(resultSet.next()){
            slot = mapSlot(resultSet);
        }

        statement.close();
        resultSet.close();
        connection.close();

        return Optional.ofNullable(slot);
    }

    private List<Record> getRecordsBySlotId(Long slotId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<org.example.orissem01.models.Record> records = new ArrayList<>();
        String sql = """
            select r.account_slot_id, r.chats_count, r.status, r.comment,
                   a.account_id, login, password, name, surname, role
            from accounts a
            join account_slot acs on a.account_id = acs.account_id
            join records r on acs.account_slot_id = r.account_slot_id
            where slot_id = ?
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, slotId);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            records.add(mapRecord(resultSet));
        }

        statement.close();
        resultSet.close();
        connection.close();

        return records;
    }

    private Record mapRecord(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Record record = new Record();
        User user = mapUser(resultSet);

        record.setId(resultSet.getLong ("account_slot_id"));

        record.setUser(user);

        record.setChatsCount(resultSet.getInt("chats_count"));
        record.setStatus(resultSet.getString("status"));
        record.setComment(resultSet.getString("comment"));
        return record;
    }

    private List<User> getUsersBySlotId(Long slotId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<User> users = new ArrayList<>();
        String sql = """
            select a.account_id, login, password, name, surname, role
            from accounts a
            join account_slot as acs on a.account_id = acs.account_id
            where slot_id = ?
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, slotId);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            users.add(mapUser(resultSet));
        }

        statement.close();
        resultSet.close();
        connection.close();

        return users;
    }

    private Slot mapSlot(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Slot slot = new Slot();
        Long id = resultSet.getLong  ("slot_id");
        slot.setId      (id);
        slot.setName    (resultSet.getString("name"));
        slot.setDate    (resultSet.getString("date"));
        slot.setTime    (resultSet.getString("time"));
        slot.setType    (resultSet.getString("type"));
        slot.setUsers(getUsersBySlotId(id));
        slot.setRecords(getRecordsBySlotId(id));
        return slot;
    }

    private User mapUser(ResultSet resultSet) throws SQLException, ClassNotFoundException {
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
