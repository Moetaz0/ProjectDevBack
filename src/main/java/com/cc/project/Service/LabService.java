package com.cc.project.Service;

import com.cc.project.Entity.Lab;
import com.cc.project.Repository.LabRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabService {
    private final LabRepository labRepository;

    public LabService(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    public Optional<Lab> getLabById(Long id) {
        return labRepository.findById(id);
    }

    public Lab saveLab(Lab lab) {
        return labRepository.save(lab);
    }

    public Lab updateLab(Long id, Lab labDetails) {
        return labRepository.findById(id).map(lab -> {
            lab.setName(labDetails.getName());
            lab.setSpecialty(labDetails.getSpecialty());
            lab.setAddress(labDetails.getAddress());
            return labRepository.save(lab);
        }).orElseThrow(() -> new RuntimeException("Lab not found with id " + id));
    }

    public void deleteLab(Long id) {
        labRepository.deleteById(id);
    }

    public List<Lab> findBySpecialty(String specialty) {
        return labRepository.findBySpecialty(specialty);
    }
}
