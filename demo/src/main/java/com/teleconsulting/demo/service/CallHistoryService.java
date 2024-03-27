package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.CallHistory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface CallHistoryService {

    public CallHistory saveCallHistory(CallHistory callHistory);
    List<CallHistory> getCallHistoryForToday();
    List<CallHistory> getCallHistoryForTodayWithinTimeRange(LocalTime startTime, LocalTime endTime);

    List<CallHistory> getCallHistoryForDoctor(Long doctorId);
    List<CallHistory> getCallHistoryForDoctorToday(Long doctorId);

    List<CallHistory> getCallHistoryForDoctorTodayWithinTimeRange(Long doctorId, LocalTime startTime, LocalTime endTime);

    List<CallHistory> getAllCallHistoryForDoctor(Long doctorId);
    void updatePrescription(Long id, String prescription);
    void updateendtime(Long id, LocalTime endtime);
    Long getPatientIdFromCallHistory(Long id);
    List<CallHistory> getCallHistoryForDoctorsWithSdid(Long sdid);

}
