package com.teleconsulting.demo.service;

import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.dto.AuthenticationResponse;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.PatientRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService{
    private  final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    private static final String key = "AP6bYQSb8OBtd6k9Xp80koDXwOwzo03V"; // 16 characters for AES-128, 24 characters for AES-192, 32 characters for AES-256

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
    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }


    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientByPhoneNumber(String phoneNumber) {
        try {
            Patient patient = patientRepository.findByPhoneNumber(encrypt(phoneNumber));
            if(patient != null)
            {
                String temp = patient.getPhoneNumber();
                try{
                    patient.setPhoneNumber(decrypt(temp));
                }catch(Exception e)
                {
                    System.out.println("\nException in PatientServiceImple findById\n"+e);
                }
            }
            return patient;
        } catch (Exception e) {
            throw new RuntimeException(e);
}
}

    @Override
    public Patient findById(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient != null)
        {
            String temp = patient.getPhoneNumber();
            try{
                patient.setPhoneNumber(decrypt(temp));
            }catch(Exception e)
            {
                System.out.println("\nException in PatientServiceImple findById\n"+e);
            }
        }
        return patient;
}

    @Override
    public Patient updatePatient(Long patientId, Pdetails pdetails) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        patient.setName(pdetails.getName());
        patient.setGender(pdetails.getGender());

        return patientRepository.save(patient);
}
    @Override
    public AuthenticationResponse saveNewPatient(Patient patient){
        Patient patient1 = patientRepository.findByEmail(patient.getEmail()).orElse(null);
        if(patient1 == null) {
            patient1.setPassword(passwordEncoder.encode(patient.getPassword()));
            patient1.setEmail(patient.getEmail());
            patient1.setName(patient.getName());
            patient1.setGender(patient.getGender());
            try{
                patient1.setPhoneNumber(encrypt(patient.getPhoneNumber()));
            }catch(Exception e){
                System.out.println("\nException in SaveNewPatient "+e+"\n");
            }
            patient1.setRole(Role.valueOf(Role.USER.toString()));
            patientRepository.save(patient1);
            return new AuthenticationResponse(null, "User Registration was Successful!!");
        }
        else
            return new AuthenticationResponse(null, "Patient Email ID already exist");

    }

    //murli
    @Override
    public Long countPatients() {
        return patientRepository.count();
    }
    //AZIZ
    @Override
    public Optional<Patient> getUserByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email).orElse(null);
        if(patient != null)
        {
            String temp = patient.getPhoneNumber();
            try{
                patient.setPhoneNumber(decrypt(temp));
            }catch(Exception e)
            {
                System.out.println("\nException in PatientServiceImple getUseByEmail\n"+e);
            }
        }
        return Optional.ofNullable(patient);
}
}
