package ma.emsi.patientsmvc.web;

import lombok.AllArgsConstructor;
import ma.emsi.patientsmvc.entities.Appointment;
import ma.emsi.patientsmvc.entities.Doctor;
import ma.emsi.patientsmvc.repositories.AppointmentRepository;
import ma.emsi.patientsmvc.repositories.DoctorRepository;
import ma.emsi.patientsmvc.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class AppointmentController {
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    AppointmentRepository appointmentRepository;

    @GetMapping(path = "/user/indexAppointments")
    public String appointments(Model model,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "5") int size,
                          @RequestParam(name = "keyword", defaultValue = "") String keyword,
                          @RequestParam(name = "confirmed", defaultValue = "false") boolean confirmed,
                          @RequestParam(name = "notConfirmed", defaultValue = "false") boolean notConfirmed
    ){

        Page<Appointment> pageAppointments;

        if (confirmed && !notConfirmed)
            pageAppointments=appointmentRepository
                    .findByConfirmed(true,PageRequest.of(page, size));

        else if (!confirmed && notConfirmed)
            pageAppointments=appointmentRepository
                    .findByConfirmed(false,PageRequest.of(page, size));
        else
            pageAppointments=appointmentRepository
                .findAll(PageRequest.of(page, size));


        model.addAttribute("listAppointments",pageAppointments.getContent());
        model.addAttribute("pages",new int[pageAppointments.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("totalPages",pageAppointments.getTotalPages());
        model.addAttribute("confirmed",confirmed);
        model.addAttribute("notConfirmed",notConfirmed);
        return "appointments";
    }

    @GetMapping("/admin/deleteAppointment")
    public String deleteAppointment(Long id, String keyword, int page){
        appointmentRepository.deleteById(id);
        return "redirect:/user/indexAppointments?page="+page+"&keyword="+keyword;
    }


    @GetMapping("/user/appointments")
    @ResponseBody
    public List<Appointment> listAppointments(){
        return appointmentRepository.findAll();
    }

    @GetMapping("/admin/formAppointments")
    public String formAppointment(Model model){
        model.addAttribute("appointment",new Appointment());
        return "formAppointments";
    }

    @PostMapping("/admin/saveAppointment")
    public String saveAppointment(Model model,
                                  @Valid Appointment appointment,
                                  BindingResult bindingResult,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "") String keyword
                                  ){
        if (bindingResult.hasErrors()) return "formAppointments";
        System.out.println(appointment.getDoctor().getId());
        System.out.println(appointment.getPatient().getId());
        appointmentRepository.save(appointment);
        return "redirect:/user/indexAppointments?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/editAppointment")
    public String editAppointment(Model model, Long id, String keyword, int page){
        Appointment appointment=appointmentRepository.findById(id).orElse(null);
        if(appointment==null) throw new RuntimeException("Appointment not found!");
        model.addAttribute("appointment",appointment);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editAppointment";
    }
}
