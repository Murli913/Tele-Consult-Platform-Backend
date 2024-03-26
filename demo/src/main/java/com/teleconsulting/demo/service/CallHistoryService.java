package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.CallHistory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface CallHistoryService {

    public CallHistory saveCallHistory(CallHistory callHistory);
    List<CallHistory> getCallHistoryForToday();
    List<CallHistory> getCallHistoryForTodayWithinTimeRange(LocalTime startTime, LocalTime endTime);

}
