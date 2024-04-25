package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.dto.DateTime;
import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.model.CallHistory;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.CallHistoryRepository;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.service.*;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.teleconsulting.demo.service.CallHandlingService.availableDoctorsQueue;

@RestController
@RequestMapping("/callhistory")
@CrossOrigin("http://localhost:5173")
public class CallHistoryController {
    private final CallHistoryService callHistoryService;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final CallHistoryRepository callHistoryRepository;
    private final CallHandlingService callHandlingService;
    private final EmailService emailService;

    private final DoctorService doctorService;

    public CallHistoryController(CallHistoryService callHistoryService, PatientService patientService, DoctorRepository doctorRepository, PatientRepository patientRepository, CallHistoryRepository callHistoryRepository, CallHandlingService callHandlingService, EmailService emailService, DoctorService doctorService) {
        this.callHistoryService = callHistoryService;
        this.patientService = patientService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.callHistoryRepository = callHistoryRepository;
        this.callHandlingService = callHandlingService;
        this.emailService = emailService;
        this.doctorService = doctorService;
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
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<CallHistory>> getCallHistoryForDoctor(
            @PathVariable Long doctorId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<CallHistory> callHistoryList;
        if (date != null) {
            // If date is provided, fetch call history for that specific date
            if (startTime != null && endTime != null) {
                // If startTime and endTime are also provided, filter call history within the specified time range
                LocalTime start = LocalTime.parse(startTime);
                LocalTime end = LocalTime.parse(endTime);
                callHistoryList = callHistoryService.getCallHistoryForDoctorTodayWithinTimeRange(doctorId, date, start, end);
            } else {
                // If startTime and endTime are not provided, fetch all call history for the doctor for that specific date
                callHistoryList = callHistoryService.getCallHistoryForDoctorToday(doctorId, date);
            }
        } else {
            // If date is not provided, fetch call history for today
            if (startTime != null && endTime != null) {
                // If startTime and endTime are provided, filter call history within the specified time range for today
                LocalTime start = LocalTime.parse(startTime);
                LocalTime end = LocalTime.parse(endTime);
                callHistoryList = callHistoryService.getCallHistoryForDoctorTodayWithinTimeRange(doctorId, LocalDate.now(), start, end);
            } else {
                // If startTime and endTime are not provided, fetch all call history for the doctor for today
                callHistoryList = callHistoryService.getCallHistoryForDoctorToday(doctorId, LocalDate.now());
            }
        }
        List<CallHistory> filteredCallHistoryList = callHistoryList.stream()
                .filter(call -> call.getEndTime() == null)
                .collect(Collectors.toList());
        return new ResponseEntity<>(filteredCallHistoryList, HttpStatus.OK);
}
    @GetMapping("/doctor/{doctorId}/all")
    public ResponseEntity<List<CallHistory>> getAllCallHistoryForDoctor(
            @PathVariable Long doctorId, @RequestParam(required = false) Integer month, @RequestParam(required = false) Integer day, @RequestParam(required = false) Integer year) {

        List<CallHistory> callHistoryList = callHistoryService.getAllCallHistoryForDoctor(doctorId);
        LocalDate today = LocalDate.now();
        callHistoryList.removeIf(call -> call.getCallDate().isAfter(today));
        List<CallHistory> filteredList = new ArrayList<>(callHistoryList);

        // Filter call history list to include records before toda
        int currentYear = today.getYear();
        callHistoryList.removeIf(call -> call.getCallDate().getYear() != currentYear);
//        int currentmonth = today.getMonthValue();
//        callHistoryList.removeIf(call -> call.getCallDate().getMonthValue() != currentmonth);

        // Filter call history list based on the provided month, day, and year parameters

        if (year != null && year >= 2020 && year <= 2050) {
            filteredList.removeIf(call -> call.getCallDate().getYear() != year);
            if (month != null && month >= 1 && month <= 12) {
                filteredList.removeIf(call -> call.getCallDate().getMonthValue() != month);
                if (day != null && day >= 1 && day <= 31) {
                    filteredList.removeIf(call -> call.getCallDate().getDayOfMonth() != day);
                }
            }
        }

        // Return filtered call history list
        return new ResponseEntity<>(filteredList, HttpStatus.OK);

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


    //aziz
    @GetMapping("/doctor/un/{doctorId}")
    public ResponseEntity<List<CallHistory>> getunfilteredCallHistoryForDoctor(
            @PathVariable Long doctorId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        List<CallHistory> callHistoryList;
        if (date != null) {
            // If date is provided, fetch call history for that specific date
            if (startTime != null && endTime != null) {
                // If startTime and endTime are also provided, filter call history within the specified time range
                LocalTime start = LocalTime.parse(startTime);
                LocalTime end = LocalTime.parse(endTime);
                callHistoryList = callHistoryService.getCallHistoryForDoctorTodayWithinTimeRange(doctorId, date, start, end);
            } else {
                // If startTime and endTime are not provided, fetch all call history for the doctor for that specific date
                callHistoryList = callHistoryService.getCallHistoryForDoctorToday(doctorId, date);
            }
        } else {
            // If date is not provided, fetch call history for today
            if (startTime != null && endTime != null) {
                // If startTime and endTime are provided, filter call history within the specified time range for today
                LocalTime start = LocalTime.parse(startTime);
                LocalTime end = LocalTime.parse(endTime);
                callHistoryList = callHistoryService.getCallHistoryForDoctorTodayWithinTimeRange(doctorId, LocalDate.now(), start, end);
            } else {
                // If startTime and endTime are not provided, fetch all call history for the doctor for today
                callHistoryList = callHistoryService.getCallHistoryForDoctorToday(doctorId, LocalDate.now());
            }
        }
        List<CallHistory> unfilteredCallHistoryList = callHistoryList.stream()
                .filter(call -> call.getEndTime() != null)
                .collect(Collectors.toList());

        return new ResponseEntity<>(unfilteredCallHistoryList, HttpStatus.OK);
    }

    @GetMapping("/fetchcalls")
    public List<?> getCallHistoryByDoctorAndPatient(
            @RequestParam Long did,
            @RequestParam Long pid) {
        Doctor doctor = doctorRepository.findById(did).orElse(null);
        Patient patient = patientRepository.findById(pid).orElse(null);
        if (doctor != null && patient != null) {
            List<?> callHistoryList = callHistoryRepository.findByDoctorAndPatient(doctor, patient);

            // Filter the list based on endTime != null
            List<?> filteredList = callHistoryList.stream()
                    .filter(callHistory -> ((CallHistory) callHistory).getEndTime() != null)
                    .collect(Collectors.toList());

            return filteredList;
        } else {
            // Handle case when doctor or patient not found
            return null;
        }
    }

    @PostMapping("/join-room") // Update incoming call
    public ResponseEntity<?> joinRoom(@RequestBody RoomJoinRequest request) {
        Doctor doctor = doctorService.findByPhoneNumber(request.getDoctorPhoneNumber());
        if (doctor != null) {
            // Check if there are available doctors in the queue
            if (!availableDoctorsQueue.isEmpty()) {
                // Dequeue the first available doctor
                Doctor availableDoctor = availableDoctorsQueue.poll();
                System.out.println("Inside callhistory joinroom");

                // Update the incoming call for the dequeued doctor
                availableDoctor.setIncomingCall(request.getPatientPhoneNumber());
                doctorService.saveDoctor(availableDoctor);

                // Return the doctor to the available doctors queue if needed
                // (for scenarios where the doctor was not immediately available

                return ResponseEntity.ok("Incoming Call status updated");
            } else {
                // No available doctors, patient will be added to the waiting queue
                Patient patient = patientRepository.findByPhoneNumber(request.getPatientPhoneNumber());
                if (patient != null) {
                    callHandlingService.addWaitingPatient(patient);
                    return ResponseEntity.ok("Doctor not available, patient added to waiting queue");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
                }

            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }

    @PutMapping("/{doctorId}/reject-call")
    public ResponseEntity<?> rejectCall(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null) {
            // Set incoming call to null
            doctor.setIncomingCall(null);
            doctorService.saveDoctor(doctor);

            // Add the doctor back to the available doctors queue
            availableDoctorsQueue.add(doctor);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }

    @PutMapping("/{patientId}") // Doctor can access
    public ResponseEntity<Patient> updatePatient(@PathVariable Long patientId, @RequestBody Pdetails pdetails) {
        Patient patient = patientService.updatePatient(patientId, pdetails );
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/sendEmailNotifications")
    public ResponseEntity<String> sendEmailNotificationsForToday() {
        try {
            List<CallHistory> appointmentsToday = callHistoryService.getCallHistoryForToday();
            System.out.print(appointmentsToday);
            for (CallHistory appointment : appointmentsToday) {
                emailService.sendAppointmentNotification(appointment);
            }
            return ResponseEntity.ok("Email notifications sent successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email notifications.");
}
}
    //murli
    @GetMapping("/all")
    public List<CallHistory> getAllCallHistory() {
        System.out.println("\nInside Call History All : GET\n");
        return callHistoryService.getAllCallHistory();
    }
    //murli
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalAppointmentsCount() {
        List<CallHistory> allCallHistory = callHistoryService.getAllCallHistory();
        long totalCount = allCallHistory.size();
        return ResponseEntity.ok(totalCount);
    }
    //murli
    @GetMapping("/{patientId}")
    public ResponseEntity<List<CallHistory>> getCallHistoryByPatientId(@PathVariable Long patientId) {
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryByPatientId(patientId);
        return ResponseEntity.ok(callHistoryList);
    }
    //murli
    @GetMapping("/doctor/{doctorId}/callhistory")
    public ResponseEntity<List<CallHistory>> getCallHistoryForDoctors(
            @PathVariable Long doctorId) {
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForDoctor(doctorId);
        return ResponseEntity.ok(callHistoryList);
    }
    //murli
    @PostMapping("/doctor/add")
    public ResponseEntity<Long> addAppointment(@RequestBody CallHistory callHistory) {
        try {
            CallHistory savedCallHistory = callHistoryService.saveCallHistory(callHistory);
            return ResponseEntity.ok(savedCallHistory.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L); // Return -1 if failed
        }
    }
    //murli
    @PutMapping("/{id}/update") // Update appointment details
    public ResponseEntity<?> updateAppointmentDetails(
            @PathVariable Long id,
            @RequestBody DateTime dateTime) {
        try {
            callHistoryService.updateAppointmentDetails(id, dateTime.getCallDate(), dateTime.getCallTime(), dateTime.getEndTime());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating appointment details");
        }
    }
    //murli
    @GetMapping("/doctor/{doctorId}/today")
    public ResponseEntity<List<CallHistory>> getTodayAppointmentsForDoctor(
            @PathVariable Long doctorId) {
        List<CallHistory> callHistoryList = callHistoryService.getTodayAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(callHistoryList);
    }
//MURLI
// Count appointments for a specific doctor
@GetMapping("/doctor/{doctorId}/appointments/count")
public ResponseEntity<Long> countAppointmentsByDoctorId(@PathVariable Long doctorId) {
    Long count = callHistoryService.countAppointmentsByDoctorId(doctorId);
    return ResponseEntity.ok(count);
}
//murli
@GetMapping("/doctor/{doctorId}/patient/count")
public ResponseEntity<Long> countPatientsByDoctorId(@PathVariable Long doctorId) {
    Long count = callHistoryService.countPatientsByDoctorId(doctorId);
    return ResponseEntity.ok(count);
}


}
