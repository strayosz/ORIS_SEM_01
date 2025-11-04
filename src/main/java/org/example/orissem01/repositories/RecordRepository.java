package org.example.orissem01.repositories;

import org.example.orissem01.models.Record;
import org.example.orissem01.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        statement.setLong  (3, record.getId());
        statement.executeUpdate();

        statement.close();
        connection.commit();
        connection.close();
    }

}
