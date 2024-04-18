package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.CallHistory;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.CallHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CallHistoryServiceImpl implements CallHistoryService{
    @Autowired
    private CallHistoryRepository callHistoryRepository;
    @Autowired
    private DoctorService doctorService; // Inject DoctorService to fetch doctors by sdid

    @Override
    public List<CallHistory> getCallHistoryForDoctorsWithSdid(Long sdid) {
        List<Long> doctorIds = doctorService.getDoctorsBySupervisorId(sdid).stream()
                .map(Doctor::getId)
                .collect(Collectors.toList());
        return callHistoryRepository.findByDoctorIdIn(doctorIds);
    }

    @Override
    public ResponseEntity<List<String>> getDoctorTimeSlots(Long doctorId, String date) {
        LocalDate callDate = LocalDate.parse(date);
        List<CallHistory> callHistoryList = callHistoryRepository.findByDoctorIdAndCallDate(doctorId, callDate);

        List<String> timeSlots = callHistoryList.stream()
                .map(CallHistory::getCallTime) // Map each CallHistory record to its call time
                .map(callTime -> callTime.toString()) // Convert each LocalTime to string
                .collect(Collectors.toList());

        return ResponseEntity.ok(timeSlots);
    }

    @Override
    public List<Object> getUpAptPat(Long patientId) {
        LocalDate today = LocalDate.now();
        List<CallHistory> callHistories = callHistoryRepository.findByPatientIdAndGreaterThanEqualAndEndTimeIsNull(patientId, today);
        System.out.println("\n\n\n Before Map \n\n\n");
        List<Object> result = callHistories.stream()
                .map(callHistory -> {
                    if (callHistory.getDoctor() != null) {
                        Map<String, Object> entry = new HashMap<>();
                        entry.put("doctorId", callHistory.getDoctor().getId());
                        entry.put("doctorName", callHistory.getDoctor().getName());
                        entry.put("doctorGender", callHistory.getDoctor().getGender());
                        entry.put("doctorPhoneNumber", callHistory.getDoctor().getPhoneNumber());
                        entry.put("doctorEmail", callHistory.getDoctor().getEmail());
                        entry.put("callDate", callHistory.getCallDate());
                        entry.put("callTime", callHistory.getCallTime());
                        entry.put("prescription", callHistory.getPrescription());
                        entry.put("endTime", callHistory.getEndTime());
                        return entry;
                    } else {
                        // Handle case where doctor is null
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Filter out null entries
                .collect(Collectors.toList());
        System.out.println("\n\n\n After Map \n\n\n");
        return result;
    }

    @Override
    public List<Object> getPastAptPat(Long patientId) {
        List<CallHistory> callHistories = callHistoryRepository.findByPatientIdAndEndTimeIsNotNull(patientId);

        List<Object> result = callHistories.stream()
                .map(callHistory -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("doctorId", callHistory.getDoctor().getId());
                    entry.put("doctorName", callHistory.getDoctor().getName());
                    entry.put("doctorGender", callHistory.getDoctor().getGender());
                    entry.put("doctorPhoneNumber", callHistory.getDoctor().getPhoneNumber());
                    entry.put("doctorEmail", callHistory.getDoctor().getEmail());
                    entry.put("callDate", callHistory.getCallDate());
                    entry.put("callTime", callHistory.getCallTime());
                    entry.put("prescription", callHistory.getPrescription());
                    entry.put("endTime", callHistory.getEndTime());
                    return entry;
                })
                .collect(Collectors.toList());

        return result;
    }


    @Override
    public List<CallHistory> getCallHistoryForDoctor(Long doctorId) {
        return callHistoryRepository.findByDoctorId(doctorId);
    }
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
    @Override
    public List<CallHistory> getCallHistoryForDoctorToday(Long doctorId) {
        return callHistoryRepository.findByDoctorIdAndCallDate(doctorId, LocalDate.now());
}

    @Override
    public List<CallHistory> getCallHistoryForDoctorTodayWithinTimeRange(Long doctorId, LocalTime startTime, LocalTime endTime) {
        LocalDate today = LocalDate.now();
        return callHistoryRepository.findByDoctorIdAndCallDateAndCallTimeBetween(doctorId, today, startTime,endTime);
}
    @Override
    public void updatePrescription(Long id, String prescription) {
        Optional<CallHistory> optionalCallHistory = callHistoryRepository.findById(id);
        if (optionalCallHistory.isPresent()) {
            CallHistory callHistory = optionalCallHistory.get();
            callHistory.setPrescription(prescription);
            callHistoryRepository.save(callHistory);
        } else {
            throw new IllegalArgumentException("Call history entry with ID " + id + " not found");
        }
}

    @Override
    public void updateendtime(Long id, LocalTime endtime) {
        Optional<CallHistory> optionalCallHistory = callHistoryRepository.findById(id);
        if(optionalCallHistory.isPresent()) {
            CallHistory callHistory = optionalCallHistory.get();
            callHistory.setEndTime(endtime);
            callHistoryRepository.save(callHistory);
        }
        else {
            throw new IllegalArgumentException("Call history entry with ID " + id + " not found");

        }
    }

    @Override
    public Long getPatientIdFromCallHistory(Long id) {
        Optional<CallHistory> callHistoryOptional = callHistoryRepository.findById(id);
        if (callHistoryOptional.isPresent()) {
            CallHistory callHistory = callHistoryOptional.get();
            Patient patient = callHistory.getPatient();
            if (patient != null) {
                return patient.getId();
            } else {
                throw new IllegalStateException("Patient ID not found for call history entry with ID " + id);
            }
        } else {
            throw new IllegalArgumentException("Call history entry with ID " + id + " not found");
        }}

    @Override
    public List<CallHistory> getAllCallHistoryForDoctor(Long doctorId) {
        return callHistoryRepository.findByDoctorId(doctorId);
}


}
