package org.example.orissem01.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.orissem01.models.Record;
import org.example.orissem01.models.User;
import org.example.orissem01.repositories.RecordRepository;

import java.util.List;
import java.util.function.Predicate;

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

            List<Record> sheduledAndExchangedRecords =
                    getRecords(user, r -> r.getStatus().equalsIgnoreCase("запланирована")
                            || r.getStatus().equalsIgnoreCase("отдается"));
            request.setAttribute("sheduledAndExchangedRecords", sheduledAndExchangedRecords);

            List<Record> sheduledRecords =
                    getRecords(user, r -> r.getStatus().equalsIgnoreCase("запланирована"));
            request.setAttribute("sheduledRecords", sheduledRecords);

            List<Record> completedRecords =
                    getRecords(user, r -> !r.getStatus().equalsIgnoreCase("запланирована")
                            && !r.getStatus().equalsIgnoreCase("отдается"));
            request.setAttribute("completedRecords", completedRecords);

            if (sheduledRecords.isEmpty()) {
                request.setAttribute("errormessage", "Нет доступных для обмена смен");
            }

            return "/userRecords.ftl";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String updateUserRecord(HttpServletRequest request) {
        String resource = "/userRecords.ftl";
        try {
            User user = userService.findUserByLogin(request);
            List<Record> sheduledRecords =
                    getRecords(user, r -> r.getStatus().equalsIgnoreCase("запланирована"));

            Long id = Long.parseLong(request.getParameter("choosedRecordId"));
            for(Record record: sheduledRecords) {
                if (record.getId().equals(id)){
                    record.setStatus("Отдается");
                    recordRepository.updateRecord(record);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resource;
    }

    private List<Record> getRecords(User user, Predicate<? super Record> predicate) {
        return user.getRecords()
                .stream()
                .filter(predicate)
                .toList();
    }
}
