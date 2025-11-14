package org.example.orissem01.repositories;

import org.example.orissem01.models.Record;
import org.example.orissem01.models.Slot;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.interfaces.IRecordRepository;
import org.example.orissem01.repositories.mappers.EntityMapper;
import org.example.orissem01.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecordRepositoryImpl implements IRecordRepository{

    private final EntityMapper entityMapper;

    public RecordRepositoryImpl(EntityMapper entityMapper){
        this.entityMapper = entityMapper;
    }

    @Override
    public Optional<Record> findRecordById(Long id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        String sql = """
            select r.account_slot_id r_account_slot_id, r.chats_count r_chats_count,
                   r.status r_status, r.comment r_comment,
                   a.account_id a_account_id, a.login a_login, a.password a_password, a.name a_name,
                   a.surname a_surname, a.role a_role,
                   s.slot_id s_slot_id, s.name s_name, s.date s_date, s.time s_time, s.type s_type
            from records r
            join public.account_slot acs on r.account_slot_id = acs.account_slot_id
            join public.accounts a on a.account_id = acs.account_id
            join public.slots s on acs.slot_id = s.slot_id
            where r.account_slot_id = ?
            """;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, id);
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

    @Override
    public List<Record> getRecords() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<Record> records = new ArrayList<>();
        String sql = """
                        select r.account_slot_id r_account_slot_id, r.chats_count r_chats_count,
                               r.status r_status, r.comment r_comment,
                               a.account_id a_account_id, a.login a_login, a.password a_password, a.name a_name,
                               a.surname a_surname, a.role a_role,
                               s.slot_id s_slot_id, s.name s_name, s.date s_date, s.time s_time, s.type s_type
                        from records r
                        join public.account_slot acs on r.account_slot_id = acs.account_slot_id
                        join public.accounts a on a.account_id = acs.account_id
                        join public.slots s on acs.slot_id = s.slot_id
                        """;
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            records.add(mapRecord(resultSet));
        }

        statement.close();
        resultSet.close();
        connection.close();

        return records;
    }

    @Override
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

    @Override
    public Record mapRecord(ResultSet resultSet) throws SQLException{
        Record record = entityMapper.mapRecordDefault(resultSet);
        User user = entityMapper.mapUserDefault(resultSet);
        Slot slot = entityMapper.mapSlotDefault(resultSet);
        record.setUser(user);
        record.setSlot(slot);
        return record;
    }
}
