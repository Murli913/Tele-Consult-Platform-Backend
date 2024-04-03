package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.model.CallHistory;
import com.teleconsulting.demo.service.CallHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/callhistory")
public class CallHistoryController {
    private final CallHistoryService callHistoryService;

    public CallHistoryController(CallHistoryService callHistoryService) {
        this.callHistoryService = callHistoryService;
    }

    @PostMapping("/add") // Doctor
    public Long add(@RequestBody CallHistory callHistory) {
        CallHistory savedCallHistory = callHistoryService.saveCallHistory(callHistory);
        return savedCallHistory.getId();
    }
    @GetMapping("/today") // TRASH
    public ResponseEntity<List<CallHistory>> getCallHistoryForToday() {
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForToday();
        return new ResponseEntity<>(callHistoryList, HttpStatus.OK);
    }
    @GetMapping("/today/search") // TRASH
    public ResponseEntity<List<CallHistory>> searchCallHistory(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        // Get today's date
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForTodayWithinTimeRange(start, end);
        return ResponseEntity.ok(callHistoryList);
    }
    @GetMapping("/getappointment/{id}") // Doctor Past Appointment ( Call history )
    public ResponseEntity<List<CallHistory>> getCallHistoryForDoctor(@PathVariable("id") Long doctorId) {
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForDoctor(doctorId);
        return ResponseEntity.ok(callHistoryList);
    }
    @GetMapping("/seniordoctors/{sdid}") // Past appointment of Sr Doc and Doc under Sr Doc
    public ResponseEntity<List<CallHistory>> getCallHistoryForDoctorsWithSdid(@PathVariable("sdid") Long sdid) {
            List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForDoctorsWithSdid(sdid);
            return ResponseEntity.ok(callHistoryList);
    }
    @GetMapping("/{id}/patientId") // Get Appointment for given patient
    public ResponseEntity<Long> getPatientIdFromCallHistory(@PathVariable Long id) {
        Long patientId = callHistoryService.getPatientIdFromCallHistory(id);
        return new ResponseEntity<>(patientId, HttpStatus.OK);
    }
    @PutMapping("/{cid}/updateendtime/{endtime}") // Update End time when call ends
    public ResponseEntity<?> updateendtime(@PathVariable Long cid, @PathVariable LocalTime endtime) {
        try {
            callHistoryService.updateendtime(cid, endtime);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating prescription");
        }
    }
    @PutMapping("/{cid}/update-prescription/{prescription}") // Update Prescription when call end
    public ResponseEntity<?> updatePrescription(@PathVariable Long cid, @PathVariable String prescription) {
        try {
            callHistoryService.updatePrescription(cid, prescription);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating prescription");
        }
    }
    @GetMapping("/doctor/{doctorId}") // Get call history for given Doc
    public ResponseEntity<List<CallHistory>> getCallHistoryForDoctorToday(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<CallHistory> callHistoryList;
        if (startTime != null && endTime != null) {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            callHistoryList = callHistoryService.getCallHistoryForDoctorTodayWithinTimeRange(doctorId, start, end);
        } else {
            callHistoryList = callHistoryService.getCallHistoryForDoctorToday(doctorId);
        }
        return new ResponseEntity<>(callHistoryList, HttpStatus.OK);
    }
    @GetMapping("/doctor/{doctorId}/all") // Past history for given Doc
    public ResponseEntity<List<CallHistory>> getAllCallHistoryForDoctor(
            @PathVariable Long doctorId) {
        List<CallHistory> callHistoryList = callHistoryService.getAllCallHistoryForDoctor(doctorId);
        return new ResponseEntity<>(callHistoryList, HttpStatus.OK);
    }
    @PostMapping("/schedule") // Doctor
    public ResponseEntity<String> scheduleCall(@RequestBody CallHistory callHistory) {
        try {
            callHistoryService.saveCallHistory(callHistory);
            return ResponseEntity.ok("Call scheduled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error scheduling call");
        }
    }
}
