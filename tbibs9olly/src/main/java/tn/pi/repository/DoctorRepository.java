package tn.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pi.entity.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
     Doctor findByName(String name);
     Optional<Doctor> findById(Long id);
     List<Doctor> findAll();


}