package ma.emsi.patientsmvc.repositories;

import ma.emsi.patientsmvc.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
