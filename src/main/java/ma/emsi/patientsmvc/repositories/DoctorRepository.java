package ma.emsi.patientsmvc.repositories;

import ma.emsi.patientsmvc.entities.Doctor;
import ma.emsi.patientsmvc.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findByNomContains(String kw, Pageable pageable);
}
