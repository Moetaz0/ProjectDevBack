package com.cc.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String specialty; // e.g. blood test, x-ray
    // Lab.java
    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "lab" }, allowSetters = true) // or @JsonIgnore if you don’t need results here
    private List<LabResult> results = new ArrayList<>();
}
