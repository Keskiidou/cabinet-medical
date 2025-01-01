package tn.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pi.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
     Doctor findByName(String name);
}