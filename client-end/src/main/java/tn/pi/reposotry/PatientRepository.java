package tn.pi.reposotry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.pi.entity.Patient;
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
