package com.cc.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "medical_histories")
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 2000)
    private String conditions; // comma or newline separated

    @Column(length = 10)
    private String gender;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(length = 1500)
    private String allergies;
    @Column(length = 1500)
    private String surgeries;

    @Column(length = 1500)
    private String medications;

    @Column(length = 50)
    private String smokingStatus;

    @Column(length = 50)
    private String alcoholConsumption;

    private Integer heightCm;
    private Double weightKg;

    @Column(length = 10)
    private String bloodType;

    @Column(length = 4000)
    private String notesDaySingIn;

    @Column(name = "date_of_sign_in")
    private Date dateOfSignIn;

    @Column(name = "rated_health")
    private Integer ratedHealth;

    private Instant createdAt;
    private Instant updatedAt;
    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabResult> labResults = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }

    public void setUser(User user) {
        this.user = user;
    }

}
