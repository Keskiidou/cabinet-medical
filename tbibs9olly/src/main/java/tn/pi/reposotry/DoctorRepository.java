package tn.pi.reposotry;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pi.entity.Doctor;


import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
     Doctor findByName(String name);
}