package ma.emsi.patientsmvc.web;

import lombok.AllArgsConstructor;
import ma.emsi.patientsmvc.entities.Doctor;
import ma.emsi.patientsmvc.entities.Patient;
import ma.emsi.patientsmvc.repositories.DoctorRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@AllArgsConstructor
public class DoctorController {
    DoctorRepository doctorRepository;

    @GetMapping(path = "/user/indexDoctors")
    public String doctors(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "5") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           @RequestParam(name = "active", defaultValue = "false") boolean active,
                           @RequestParam(name = "inactive", defaultValue = "false") boolean inactive
                            ){

        Page<Doctor> pageDoctors=doctorRepository
                .findByNomContainsOrSpecialityContains(keyword,keyword, PageRequest.of(page, size));

        if (active && !inactive)
            pageDoctors=doctorRepository
                    .findByNomContainsAndActiveIsOrSpecialityContainsAndActiveIs(keyword,
                            true,
                            keyword,
                            true,
                            PageRequest.of(page, size));

        else if (inactive && !active)
            pageDoctors=doctorRepository
                    .findByNomContainsAndActiveIsOrSpecialityContainsAndActiveIs(keyword,
                            false,
                            keyword,
                            false,
                            PageRequest.of(page, size));


        if (pageDoctors.isEmpty())
            pageDoctors=doctorRepository.findById(Long.parseLong(keyword), PageRequest.of(page, size));

        model.addAttribute("listDoctors",pageDoctors.getContent());
        model.addAttribute("pages",new int[pageDoctors.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("totalPages",pageDoctors.getTotalPages());
        model.addAttribute("active",active);
        model.addAttribute("inactive",inactive);
        return "doctors";
    }

    @GetMapping("/admin/deleteDoctor")
    public String deleteDoctor(Long id, String keyword, int page){
        doctorRepository.deleteById(id);
        return "redirect:/user/indexDoctors?page="+page+"&keyword="+keyword;
    }


    @GetMapping("/user/doctors")
    @ResponseBody
    public List<Doctor> listDoctors(){
        return doctorRepository.findAll();
    }

    @GetMapping("/admin/formDoctors")
    public String formDoctor(Model model){
        model.addAttribute("doctor",new Doctor());
        return "formDoctors";
    }

    @PostMapping("/admin/saveDoctor")
    public String saveDoctor(Model model,
                       @Valid Doctor doctor,
                       BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formDoctors";
        doctorRepository.save(doctor);
        return "redirect:/user/indexDoctors?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/editDoctor")
    public String editDoctor(Model model, Long id, String keyword, int page){
        Doctor doctor=doctorRepository.findById(id).orElse(null);
        if(doctor==null) throw new RuntimeException("Doctor not found!");
        model.addAttribute("doctor",doctor);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editDoctor";
    }


}
