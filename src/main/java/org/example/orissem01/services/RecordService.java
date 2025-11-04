package org.example.orissem01.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.orissem01.models.Record;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.RecordRepository;

import java.util.List;

public class RecordService {
    private final RecordRepository recordRepository;
    private final UserService userService;

    public RecordService(){
        this.recordRepository = new RecordRepository();
        this.userService = new UserService();
    }

    public String getUserRecords(HttpServletRequest request) {
        User user = userService.findUserByLogin(request);
        try {
            List<Record> sheduledAndExchangedRecords = recordRepository.getSheduledAndExchangedRecordsByUserId(user.getId());
            request.setAttribute("sheduledAndExchangedRecords", sheduledAndExchangedRecords);

            List<Record> sheduledRecords = recordRepository.getSheduledRecordsByUserId(user.getId());
            request.setAttribute("sheduledRecords", sheduledRecords);

            List<Record> completedRecords = recordRepository.getCompletedRecordsByUserId(user.getId());
            request.setAttribute("completedRecords", completedRecords);

            return "/userRecords.ftl";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String updateUserRecord(HttpServletRequest request) {
        String resource = "/userRecords.ftl";
        try {
            User user = userService.findUserByLogin(request);
            List<Record> sheduledRecords = recordRepository.getSheduledRecordsByUserId(user.getId());
            if (sheduledRecords.isEmpty()) {
                request.setAttribute("errormessage", "Нет доступных для обмена смен");
                return resource;
            }
            Long id = Long.parseLong(request.getParameter("choosedRecordId"));
            for(Record record: sheduledRecords) {
                if (record.getUserSlot().getId().equals(id)){
                    record.setStatus("Отдается");
                    recordRepository.updateRecord(record);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resource;
    }
}
