package com.teleconsulting.demo.repository;

import com.teleconsulting.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySupervisorDoctorId(Long supervisorId);
    Doctor findByPhoneNumber(String phoneNumber);
    Optional<Doctor> findByEmail(String email);
    @Query("SELECT d.id, d.totalRating, d.appointmentCount FROM Doctor d")
    List<Object []> getRatings();
    @Query("SELECT d FROM Doctor d WHERE d.supervisorDoctor IS NULL")
    List<Doctor>findAllSrDoc();
    List<Doctor> findByIncomingCallIsNull();
    //teja
    List<Doctor> findBySupervisorDoctorIsNull();
    @Query("SELECT d FROM Doctor d WHERE d.supervisorDoctor IS NULL AND d.deleteFlag = FALSE")
    List<Doctor> findAllSrDocs();
    @Query("SELECT d FROM Doctor d WHERE d.supervisorDoctor IS NOT NULL AND d.deleteFlag = FALSE")
    List<Doctor> findAllDoctors();
}
