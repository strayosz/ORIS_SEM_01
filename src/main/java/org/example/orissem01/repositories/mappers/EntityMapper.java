package org.example.orissem01.repositories.mappers;

import org.example.orissem01.models.Record;
import org.example.orissem01.models.Slot;
import org.example.orissem01.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityMapper {
    public User mapUserDefault(ResultSet resultSet) throws SQLException {
        User user = new User();
        Long id = resultSet.getLong  ("a_account_id");
        user.setId      (id);
        user.setLogin   (resultSet.getString("a_login"));
        user.setPassword(resultSet.getString("a_password"));
        user.setName    (resultSet.getString("a_name"));
        user.setSurname (resultSet.getString("a_surname"));
        user.setRole    (resultSet.getString("a_role"));
        return user;
    }

    public Slot mapSlotDefault(ResultSet resultSet) throws SQLException {
        Slot slot = new Slot();
        slot.setId      (resultSet.getLong  ("s_slot_id"));
        slot.setName    (resultSet.getString("s_name"));
        slot.setDate    (resultSet.getString("s_date"));
        slot.setTime    (resultSet.getString("s_time"));
        slot.setType    (resultSet.getString("s_type"));
        return slot;
    }

    public Record mapRecordDefault(ResultSet resultSet) throws SQLException {
        Record record = new Record();
        record.setId(resultSet.getLong ("r_account_slot_id"));
        record.setChatsCount(resultSet.getInt("r_chats_count"));
        record.setStatus(resultSet.getString("r_status"));
        record.setComment(resultSet.getString("r_comment"));
        return record;
    }
}
