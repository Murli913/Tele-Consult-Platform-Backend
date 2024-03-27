package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.model.CallHistory;
import com.teleconsulting.demo.service.CallHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/callhistory")
public class CallHistoryController {
    @Autowired
    private CallHistoryService callHistoryService;



    @PostMapping("/add")
    public String add(@RequestBody CallHistory callHistory)
    {
        callHistoryService.saveCallHistory(callHistory);
        return "New CallHistory added";
    }

    @GetMapping("/today")
    public ResponseEntity<List<CallHistory>> getCallHistoryForToday() {
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForToday();
        return new ResponseEntity<>(callHistoryList, HttpStatus.OK);
    }

    @GetMapping("/today/search")
    public ResponseEntity<List<CallHistory>> searchCallHistory(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        // Get today's date
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForTodayWithinTimeRange(start, end);
        return ResponseEntity.ok(callHistoryList);
    }

    @GetMapping("/getappointment/{id}")
    public ResponseEntity<List<CallHistory>> getCallHistoryForDoctor(@PathVariable("id") Long doctorId) {
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForDoctor(doctorId);
        return ResponseEntity.ok(callHistoryList);
    }

    @GetMapping("/seniordoctors/{sdid}")
    public ResponseEntity<List<CallHistory>> getCallHistoryForDoctorsWithSdid(@PathVariable("sdid") Long sdid) {
            List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForDoctorsWithSdid(sdid);
            return ResponseEntity.ok(callHistoryList);
    }

    @GetMapping("/{id}/patientId")
    public ResponseEntity<Long> getPatientIdFromCallHistory(@PathVariable Long id) {
        Long patientId = callHistoryService.getPatientIdFromCallHistory(id);
        return new ResponseEntity<>(patientId, HttpStatus.OK);
}

    @PutMapping("/{cid}/updateendtime/{endtime}")
    public ResponseEntity<?> updateendtime(@PathVariable Long cid, @PathVariable LocalTime endtime) {
        try {
            callHistoryService.updateendtime(cid, endtime);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating prescription");
}
}

    @PutMapping("/{cid}/update-prescription/{prescription}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long cid, @PathVariable String prescription) {
        try {
            callHistoryService.updatePrescription(cid, prescription);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating prescription");
}
}



}
