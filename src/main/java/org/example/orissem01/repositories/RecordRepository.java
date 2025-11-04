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

public class RecordRepository {

//    public List<Record> getRecords() throws SQLException, ClassNotFoundException {
//        Connection connection = DBConnection.getConnection();
//        List<UserSlot> userSlot = new ArrayList<>();
//        String sql = """
//                        select account_slot_id, account_id, slot_id
//                        from account_slot
//                        """;
//        PreparedStatement statement = connection.prepareStatement(sql);
//        ResultSet resultSet = statement.executeQuery();
//
//        while(resultSet.next()){
//            userSlot.add(mapUserSlot(resultSet));
//        }
//
//        statement.close();
//        resultSet.close();
//        connection.close();
//
//        return userSlot;
//    }

    public List<Record> getSheduledAndExchangedRecordsByUserId(Long userId) throws SQLException, ClassNotFoundException {
        String sql = """
            select r.account_slot_id, r.chats_count, r.status, r.comment,
                   s.slot_id, s.name, s.date, s.time, s.type
            from slots s
            join account_slot acs on s.slot_id = acs.slot_id
            join records r on acs.account_slot_id = r.account_slot_id
            where account_id = ? AND (lower(r.status) = 'запланирована' OR lower(r.status) = 'отдается')
            AND s.date >= current_date AND s.time >= current_time
            order by date, time;
            """;
        return getRecordsByUserId(userId, sql);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        RecordRepository recordRepository = new RecordRepository();
        System.out.println(recordRepository.getSheduledAndExchangedRecordsByUserId(1000000000000000000L));
    }

    public List<Record> getSheduledRecordsByUserId(Long userId) throws SQLException, ClassNotFoundException {
        String sql = """
            select r.account_slot_id, r.chats_count, r.status, r.comment,
                   s.slot_id, s.name, s.date, s.time, s.type
            from slots s
            join account_slot acs on s.slot_id = acs.slot_id
            join records r on acs.account_slot_id = r.account_slot_id
            where account_id = ? AND (lower(r.status) = 'запланирована')
            AND s.date >= current_date AND s.time >= current_time
            order by date, time;
            """;
        return getRecordsByUserId(userId, sql);
    }

    public List<Record> getCompletedRecordsByUserId(Long userId) throws SQLException, ClassNotFoundException {
        String sql = """
            select r.account_slot_id, r.chats_count, r.status, r.comment,
                   s.slot_id, s.name, s.date, s.time, s.type
            from slots s
            join account_slot acs on s.slot_id = acs.slot_id
            join records r on acs.account_slot_id = r.account_slot_id
            where account_id = ? AND lower(r.status) != 'запланирована' AND lower(r.status) != 'отдается'
            order by date, time;
            """;
        return getRecordsByUserId(userId, sql);
    }

    private List<Record> getRecordsByUserId(Long userId, String sql) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<Record> records = new ArrayList<>();
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

    public void updateRecord(Record record) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        String sql = """
            update records
            set status = ?, comment = ?
            where account_slot_id = ?;
            """;

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, record.getStatus());
        statement.setString(2, record.getComment());
        statement.setLong  (3, record.getUserSlot().getId());
        statement.executeUpdate();

        statement.close();
        connection.commit();
        connection.close();
    }

    public Optional<Record> findRecordByUserIdAndDate(Long userId, String date, String time) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        String sql = """
            select r.account_slot_id, r.chats_count, r.status, r.comment,
                   s.name, s.date, s.time, s.type
            from slots s
                     join account_slot acs on s.slot_id = acs.slot_id
                     join records r on acs.account_slot_id = r.account_slot_id
            where account_id = ? and date = ? and time = ?
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, userId);
        statement.setString(2, date);
        statement.setString(3, time);
        ResultSet resultSet = statement.executeQuery();

        Record record = null;
        if(resultSet.next()){
            record = mapRecord(resultSet);
        }

        statement.close();
        resultSet.close();
        connection.close();

        return Optional.ofNullable(record);
    }

    private Record mapRecord(ResultSet resultSet) throws SQLException {
        Record record = new Record();
        Slot slot = new Slot();
        record.setSlot(slot);

        record.getUserSlot().setId(resultSet.getLong ("account_slot_id"));

        record.getUserSlot().getSlot().setId(resultSet.getLong("slot_id"));
        record.getUserSlot().getSlot().setName(resultSet.getString("name"));
        record.getUserSlot().getSlot().setDate(resultSet.getString("date"));
        record.getUserSlot().getSlot().setTime(resultSet.getString("time"));
        record.getUserSlot().getSlot().setType(resultSet.getString("type"));

        record.setChatsCount(resultSet.getInt("chats_count"));
        record.setStatus(resultSet.getString("status"));
        record.setComment(resultSet.getString("comment"));
        return record;
    }

    private Record mapRecordFull(ResultSet resultSet) throws SQLException {
        Record record = new Record();
        UserSlot userSlot = new UserSlot();
        Slot slot = new Slot();
        User user = new User();
        userSlot.setSlot(slot);
        userSlot.setUser(user);
        record.setUserSlot(userSlot);

        record.getUserSlot().setId(resultSet.getLong ("account_slot_id"));

        record.getUserSlot().getSlot().setName(resultSet.getString("name"));
        record.getUserSlot().getSlot().setDate(resultSet.getString("date"));
        record.getUserSlot().getSlot().setTime(resultSet.getString("time"));
        record.getUserSlot().getSlot().setType(resultSet.getString("type"));

        record.getUserSlot().getUser().setId();
        record.getUserSlot().getUser().setName();
        record.getUserSlot().getUser();
        record.getUserSlot().getUser();
        record.getUserSlot().getUser();

        record.setChatsCount(resultSet.getInt("chats_count"));
        record.setStatus(resultSet.getString("status"));
        record.setComment(resultSet.getString("comment"));
        return record;
    }
}
