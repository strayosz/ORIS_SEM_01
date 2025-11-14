package org.example.orissem01.repositories;

import org.example.orissem01.models.Slot;
import org.example.orissem01.models.Transaction;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.interfaces.ITransactionRepository;
import org.example.orissem01.repositories.mappers.EntityMapper;
import org.example.orissem01.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements ITransactionRepository {

    private final EntityMapper entityMapper;

    public TransactionRepositoryImpl(EntityMapper entityMapper){
        this.entityMapper = entityMapper;
    }

    @Override
    public List<Transaction> getTransactions() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
             select t.transaction_id t_transaction_id, t.from_account_id t_from_account_id,
                    t.to_account_id t_to_account_id, t.slot_id t_slot_id,
                    t.date t_date, t.time t_time, t.comment t_comment,
             
                    a.account_id a_account_id, a.login a_login, a.password a_password,
                    a.name a_name, a.surname a_surname, a.role a_role,
             
                    b.account_id b_account_id, b.login b_login, b.password b_password,
                    b.name b_name, b.surname b_surname, b.role b_role,
             
                    s.slot_id s_slot_id, s.name s_name, s.date s_date, s.time s_time, s.type s_type
                    from transactions t
                    join accounts a on t.from_account_id = a.account_id
                    join accounts b on t.to_account_id = b.account_id
                    join slots s on t.slot_id = s.slot_id
             """;
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            transactions.add(mapTransaction(resultSet));
        }

        statement.close();
        resultSet.close();
        connection.close();

        return transactions;
    }

    @Override
    public void addTransaction(Transaction transaction) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        //Вытаскиваем айди
        String sqlSelectId = "select id from nextval('transaction_id_seq') as id";
        PreparedStatement statement = connection.prepareStatement(sqlSelectId);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()){
            Long id = resultSet.getLong("id");
            transaction.setId(id);
        }

        resultSet.close();
        statement.close();

        Long fromUserid = transaction.getFromUser().getId();
        Long toUserid = transaction.getToUser().getId();
        Long slotId = transaction.getSlot().getId();

        //Меняем владельца
        String sqlUpdate = """
                update account_slot
                set account_id = ?
                where account_id = ? AND slot_id = ?;
                """;
        statement = connection.prepareStatement(sqlUpdate);
        statement.setLong(1, toUserid);
        statement.setLong(2, fromUserid);
        statement.setLong(3, slotId);

        statement.executeUpdate();
        statement.close();

        //Меняем статус
        String sqlUpdateStaus = """
                update records
                set status = 'Запланирована'
                where account_slot_id =
                      (SELECT account_slot_id
                       from account_slot
                       where account_id = ? AND slot_id = ?)
                """;
        statement = connection.prepareStatement(sqlUpdateStaus);
        statement.setLong(1, toUserid);
        statement.setLong(2, slotId);

        statement.executeUpdate();
        statement.close();

        //Вставляем транзакцию
        String sqlInsert = """
            insert into transactions(transaction_id, from_account_id, to_account_id, slot_id, comment)
            VALUES (?, ?, ?, ?, ?);
            """;

        statement = connection.prepareStatement(sqlInsert);
        statement.setLong  (1, transaction.getId());
        statement.setLong  (2, fromUserid);
        statement.setLong  (3, toUserid);
        statement.setLong  (4, slotId);
        statement.setString(5, transaction.getComment());

        statement.executeUpdate();
        statement.close();

        //Вытаскиваем дату и время
        String sqlSelect = "select date, time from transactions where transaction_id = ?";
        statement = connection.prepareStatement(sqlSelect);
        statement.setLong(1, transaction.getId());
        resultSet = statement.executeQuery();

        if (resultSet.next()){
            transaction.setDate(resultSet.getString("date"));
            transaction.setTime(resultSet.getString("time"));
        }

        resultSet.close();
        statement.close();

        connection.commit();
        connection.close();
    }

    @Override
    public Transaction mapTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        User fromUser = entityMapper.mapUserDefault(resultSet);
        User toUser = new User();
        Slot slot = entityMapper.mapSlotDefault(resultSet);

        toUser.setId(resultSet.getLong("b_account_id"));
        toUser.setLogin(resultSet.getString("b_login"));
        toUser.setPassword(resultSet.getString("b_password"));
        toUser.setName(resultSet.getString("b_name"));
        toUser.setSurname(resultSet.getString("b_surname"));
        toUser.setRole(resultSet.getString("b_role"));

        String date = resultSet.getString("t_date");
        String time = resultSet.getString("t_time");
        String comment = resultSet.getString("t_comment");

        transaction.setId(resultSet.getLong("t_transaction_id"));
        transaction.setFromUser(fromUser);
        transaction.setToUser(toUser);
        transaction.setSlot(slot);
        transaction.setDate(date);
        transaction.setTime(time.split("\\.")[0]);
        transaction.setComment(comment);

        return transaction;
    }
}
