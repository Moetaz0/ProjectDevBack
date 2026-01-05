package com.cc.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialization;
    private String bio;
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    // hide appointments when serializing doctor
    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private List<Appointment> appointments;
}
