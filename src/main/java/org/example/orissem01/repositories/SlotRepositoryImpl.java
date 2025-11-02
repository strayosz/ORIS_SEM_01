package org.example.orissem01.repositories;

import org.example.orissem01.models.Slot;
import org.example.orissem01.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        statement.setLong  (1, slot.getId());
        statement.setString(2, slot.getName());
        statement.setString(3, slot.getDate());
        statement.setString(4, slot.getTime());
        statement.setString(5, slot.getType());
        statement.executeUpdate();

        statement.close();
        connection.commit();
        connection.close();
    }


}
