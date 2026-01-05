package com.cc.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
public class LabResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName;
    @Column(name = "result_file_url", length = 512)
    private String resultFileUrl;
    private LocalDate date;

    // LabResult.java
    @ManyToOne
    @JoinColumn(name = "lab_id")
    @JsonIgnoreProperties(value = { "results" }, allowSetters = true) // prevent lab->results->lab recursion
    private Lab lab;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    @JsonIgnoreProperties(value = { "labResults", "appointments" }, allowSetters = true)
    private MedicalHistory medicalHistory;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

}
