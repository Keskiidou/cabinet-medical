package tn.pi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.pi.entity.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find patient by email (case-insensitive)
    Optional<Patient> findByEmailIgnoreCase(String email);

    // Find patients with name containing a keyword (case-insensitive) and paginate results
    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);




    // Find patients sorted by name in ascending order
    List<Patient> findAllByOrderByNameAsc();
}
