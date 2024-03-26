package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.CallHistory;
import com.teleconsulting.demo.repository.CallHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CallHistoryServiceImpl implements CallHistoryService{
    @Autowired
    private CallHistoryRepository callHistoryRepository;


    @Override
    public CallHistory saveCallHistory(CallHistory callHistory) {
        return callHistoryRepository.save(callHistory);
    }

    @Override
    public List<CallHistory> getCallHistoryForToday() {
        return callHistoryRepository.findByCallDate(LocalDate.now());
    }

    @Override
    public List<CallHistory> getCallHistoryForTodayWithinTimeRange(LocalTime startTime, LocalTime endTime) {
        LocalDate today = LocalDate.now();
        return callHistoryRepository.findByCallDateAndCallTimeBetween(today, startTime, endTime);
    }


}
