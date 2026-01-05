package com.cc.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;
    private String notes;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonManagedReference
    @JsonBackReference
    private User client;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    @JsonManagedReference
    @JsonIgnore
    private Doctor doctor;

    // Appointment.java
    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    @JsonIgnoreProperties(value = { "labResults", "appointments" }, allowSetters = true)
    private MedicalHistory medicalHistory;

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
