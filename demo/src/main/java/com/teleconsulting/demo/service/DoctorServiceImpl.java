package com.teleconsulting.demo.service;

import com.teleconsulting.demo.dto.Ddetails;
import com.teleconsulting.demo.dto.DoctorRating;
import com.teleconsulting.demo.dto.RegDoc;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.dto.AuthenticationResponse;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.DoctorRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    private static final String key = "AP6bYQSb8OBtd6k9Xp80koDXwOwzo03V";
    public static String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes);
    }

    @Override
    public List<Doctor> getDoctorsUnderSeniorDoctor(Long supervisorId) {
        System.out.println("\nInside getDoctorBySup DoctorServiceImpl\n");
        List<Doctor> doctors = doctorRepository.findBySupervisorDoctorId(supervisorId);
        for(Doctor doctor : doctors){
            if(!doctor.isDeleteFlag()) {
                String temp = doctor.getPhoneNumber();
                try{
                    doctor.setPhoneNumber(decrypt(temp));
                }catch(Exception e)
                {
                    System.out.println("\nException in DocServiceImple\n"+e);
                }
            }
        }
       return doctors;
    }

    @Override
    public Doctor getById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        String newPhone = doctor.getPhoneNumber();
        doctor.setPhoneNumber(passwordEncoder.encode(newPhone));
        return doctorRepository.save(doctor);
    }
    @Override
    public AuthenticationResponse saveNewDoctor(RegDoc regDoc) {
        Doctor doctor2 = doctorRepository.findByEmail(regDoc.getEmail()).orElse(null);
        if(doctor2 == null)
        {
            Doctor doctor1 = new Doctor();
            doctor1.setName(regDoc.getName());
            doctor1.setEmail(regDoc.getEmail());
            doctor1.setGender(regDoc.getGender());
            doctor1.setPassword(passwordEncoder.encode(regDoc.getPassword()));
            try{
                doctor1.setPhoneNumber(decrypt(regDoc.getPhoneNumber()));
            }catch(Exception e)
            {
                System.out.println(e);
            }
            if(regDoc.getSupervisorDoctor() == null)
            {
                System.out.println("Hello Sr Doc \n");
                doctor1.setRole(Role.valueOf(Role.SRDOC.toString()));
                System.out.println("\nRole is "+Role.valueOf(Role.SRDOC.toString()));
            }
            else
            {
                doctor1.setRole(Role.valueOf(Role.DOCTOR.toString()));
            }
            if (regDoc.getSupervisorDoctor() != null) {
                Doctor supervisorDoctor = doctorRepository.findById(regDoc.getSupervisorDoctor()).orElse(null);
                doctor1.setSupervisorDoctor(supervisorDoctor);
            }
            else
            {
                System.out.println("Super is set to null\n");
                doctor1.setSupervisorDoctor(null);
            }
            doctor1.setIncomingCall(null);
            doctor1.setDeleteFlag(false);
            doctor1.setAvailability(true);
            doctorRepository.save(doctor1);
            return new AuthenticationResponse(null, "Doctor Registration was Successful");
        }
        else
            return new AuthenticationResponse(null, "Email ID already exist!!");
    }

    @Override
    public List<Doctor> getAllDoctors() {

        List<Doctor> doctors = doctorRepository.findAllDoctors();
        for(Doctor doctor : doctors){
            String temp = doctor.getPhoneNumber();
            try{
                doctor.setPhoneNumber(decrypt(temp));
            }catch(Exception e)
            {
                System.out.println("\nException in PatientServiceImple findById\n"+e);
            }
        }
        return doctors;
    }
    @Override
    public Doctor findByPhoneNumber(String phoneNumber) {
        String newPhone = null;
        try {
            newPhone = encrypt(phoneNumber);
            Doctor doctor = doctorRepository.findByPhoneNumber(newPhone);
            if(!doctor.isDeleteFlag())
                return null;
            String temp = doctor.getPhoneNumber();
            doctor.setPhoneNumber(decrypt(temp));
            return doctor;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    @Override
    public Doctor findById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        String temp = doctor.getPhoneNumber();
        try{
            doctor.setPhoneNumber(decrypt(temp));
        }catch(Exception e)
        {
            System.out.println("\nException in Finding PhoneNumber\n"+e);
        }
      return doctor;
    }
    @Override
    public Doctor updateDoctorIncomingCall(String doctorPhoneNumber, String patientPhoneNumber) {
        String newPhone = passwordEncoder.encode(doctorPhoneNumber);
        Doctor doctor = doctorRepository.findByPhoneNumber(newPhone);
        if (doctor != null) {
            doctor.setIncomingCall(patientPhoneNumber);
            return doctorRepository.save(doctor);
        } else {
            return null;
        }
    }
    @Override
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        System.out.println("\nUpdated Doc"+updatedDoctor+"\n");
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setGender(updatedDoctor.getGender());
        try{
            existingDoctor.setPhoneNumber(decrypt(updatedDoctor.getPhoneNumber()));
        }catch(Exception e)
        {
            System.out.println("\nException in DoctorSerImpl updateDoctor\n"+e);
        }
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setPassword(passwordEncoder.encode(updatedDoctor.getPassword()));
        existingDoctor.setRole(Role.valueOf(Role.DOCTOR.toString()));
        existingDoctor.setSupervisorDoctor(updatedDoctor.getSupervisorDoctor());
        doctorRepository.save(existingDoctor);
        return existingDoctor;
    }

    @Override
    public void deleteDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if(doctor != null) {
            doctor.setDeleteFlag(true);
            doctorRepository.save(doctor);
        }
        else {
            throw new UserNotFoundException(id);
        }
    }
    //murli
    @Override
    public List<Doctor> getAllDoctorsExceptPassword() {
        List<Doctor> doctors = doctorRepository.findAll();
        for (Doctor doctor : doctors) {
            doctor.setPassword("none"); // Exclude password field
        }
        return doctors;
    }
    //murli
    @Override
    public Long countDoctors() {
        return doctorRepository.count();
    }


    @Override
    public void updateRating(Long id, int rating) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if(doctor != null)
        {
            int tempRating = doctor.getTotalRating();
            tempRating += rating;
            int tempCount = doctor.getAppointmentCount();
            tempCount++;
            doctor.setTotalRating(tempRating);
            doctor.setAppointmentCount(tempCount);
            doctorRepository.save(doctor);
        }
    }

    @Override
    public List<DoctorRating> getAllRatings() {
        List<Object[]> ratingObjects = doctorRepository.getRatings();
        List<DoctorRating> doctorRatings = new ArrayList<>();
        for (Object[] ratingObject : ratingObjects) {
            Long id = (Long) ratingObject[0];
            int rating = (int) ratingObject[1];
            int count = (int) ratingObject[2];
            doctorRatings.add(new DoctorRating(id, rating, count));
        }
        return doctorRatings;
    }
    @Override
    public Doctor updateDoctors(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        // Update the existingDoctor with data from updatedDoctor
        // You need to set all fields that can be updated
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setGender(updatedDoctor.getGender());
        existingDoctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        // Set other fields as needed

        // Save the updated doctor entity
        return doctorRepository.save(existingDoctor);
    }
    //mutli
    @Override
    public void updateDoctorSdid(Long doctorId, Long newSdid) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException( doctorId));
        Doctor supervisorDoctor = doctorRepository.findById(newSdid)
                .orElseThrow(() -> new UserNotFoundException( newSdid));
        doctor.setSupervisorDoctor(supervisorDoctor);
        doctorRepository.save(doctor);
    }


    //murli
    @Override
    public Doctor findByEmail(String email) {
        return doctorRepository.findByEmail(email).orElse(null);
    }
    //murli
    @Override
    public Doctor getDoctorNameAndPhoneNumber(Long doctorId) {
        return doctorRepository.findById(doctorId).map(doctor -> {
            Doctor doctorDetails = new Doctor();
            doctorDetails.setName(doctor.getName());
            doctorDetails.setPhoneNumber(doctor.getPhoneNumber());
            return doctorDetails;
        }).orElse(null);
    }

    //MAHARISHI
    @Override
    public List<Doctor> getAllSrDoctors() {
            List<Doctor> doctors = doctorRepository.findAllSrDocs();
            for(Doctor doctor : doctors){
                String temp = doctor.getPhoneNumber();
                try{
                    doctor.setPhoneNumber(decrypt(temp));
                }catch(Exception e)
                {
                    System.out.println("\nException in PatientServiceImple findById\n"+e);
                }
            }
           return doctors;
    }
    //aziz
    @Override
    public List<Doctor> findAllAvailableDoctors() {
        return doctorRepository.findByIncomingCallIsNull();
    }

    //AZIZ
    @Override
    public Optional<Doctor> getUserByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    //teja
    @Override
    public List<Ddetails> getSnrDoctors() {
        List<Doctor> doctors = doctorRepository.findBySupervisorDoctorIsNull();
        return doctors.stream()
                .map(doctor -> {
                    Ddetails doctorDetails = new Ddetails();
                    doctorDetails.setId(doctor.getId());
                    doctorDetails.setName(doctor.getName());
                    doctorDetails.setGender(doctor.getGender());
                    doctorDetails.setPhoneNumber(doctor.getPhoneNumber());
                    doctorDetails.setEmail(doctor.getEmail());
                    doctorDetails.setAppointmentCount(doctor.getAppointmentCount());
                    doctorDetails.setTotalRating((float) doctor.getTotalRating());
                    return doctorDetails;
                })
                .collect(Collectors.toList());
    }
}
