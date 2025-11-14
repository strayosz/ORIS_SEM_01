package org.example.orissem01.services;

import org.example.orissem01.exceptions.ConnectionException;
import org.example.orissem01.exceptions.MySQLException;
import org.example.orissem01.exceptions.NoSuchRecordException;
import org.example.orissem01.models.Record;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.RecordRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

public class RecordService {
    private final RecordRepositoryImpl recordRepository;

    public RecordService(RecordRepositoryImpl recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Record findRecordById(Long id) {
        try {
            return recordRepository.findRecordById(id)
                    .orElseThrow(() -> new NoSuchRecordException());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Record> getExchangedRecords(User user) throws ConnectionException, MySQLException {
        List<Record> records;
        try {
            records = recordRepository.getRecords()
                    .stream()
                    .filter(r -> r.getStatus().equalsIgnoreCase("отдается")
                            && !r.getUser().equals(user)
                            && !containsSlot(user, r.getSlot().getId())
                            && isTimeEqualOrAfter(r))
                    .toList();

        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
        return records;

    }

    public List<Record> getUserRecords(String recordsType, User user) {
        try {
            List<Record> records;
            if (recordsType.equals("completed")) {
                records = getRecords(user, r -> !r.getStatus().equalsIgnoreCase("запланирована")
                        && !r.getStatus().equalsIgnoreCase("отдается"));
            } else if (recordsType.equals("exchanged")) {
                records = getRecords(user, r -> r.getStatus().equalsIgnoreCase("отдается")
                        && isTimeEqualOrAfter(r));
            } else {
                records = getRecords(user, r ->
                        (r.getStatus().equalsIgnoreCase("запланирована"))
                                && isTimeEqualOrAfter(r));
            }
            return records;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void updateUserRecord(User user, String recordsType, Long id) throws MySQLException, ConnectionException {
        List<Record> records;
        try {
            if (recordsType.equals("sheduled")) {
                records =
                        getRecords(user, r -> r.getStatus().equalsIgnoreCase("запланирована"));

                for (Record record : records) {
                    if (record.getId().equals(id)) {
                        record.setStatus("Отдается");
                        recordRepository.updateRecord(record);
                    }
                }

            } else {
                records =
                        getRecords(user, r -> r.getStatus().equalsIgnoreCase("отдается"));

                for (Record record : records) {
                    if (record.getId().equals(id)) {
                        record.setStatus("Запланирована");
                        recordRepository.updateRecord(record);
                    }
                }
            }
        } catch (SQLException e) {
            throw new MySQLException();
        } catch (ClassNotFoundException e) {
            throw new ConnectionException();
        }
    }

    private boolean containsSlot(User user, Long slotId) {
        List<Record> records = user.getRecords();
        for (Record record : records) {
            if (record.getSlot().getId().equals(slotId)) {
                return true;
            }
        }
        return false;
    }

    private List<Record> getRecords(User user, Predicate<? super Record> predicate) {
        return user.getRecords()
                .stream()
                .filter(predicate)
                .toList();
    }

    private boolean isTimeEqualOrAfter(Record r) {
        return (LocalDate.parse(r.getSlot().getDate()).isAfter(LocalDate.now()))

                ||

                ((LocalDate.parse(r.getSlot().getDate()).isEqual(LocalDate.now()))
                        && (LocalTime.parse(r.getSlot().getTime()).isAfter(LocalTime.now())));
    }
}
