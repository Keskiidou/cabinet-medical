package tn.pi.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import tn.pi.entity.Patient;
import tn.pi.repository.PatientRepository;



@Controller
public class PatientController {

    private final PatientRepository patientrepo;


    // Constructor injection for the repository and password encoder
    public PatientController(PatientRepository patientrepo) {
        this.patientrepo = patientrepo;

    }

    @GetMapping("/index")
    public String index(Model model) {
        return "patients";
    }
    @GetMapping("/patientRegister")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patientRegister";
    }

    @PostMapping("/register")
    public String savePatient(Model model, @Valid Patient patient, BindingResult bindingResult) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "patientRegister";
        }
        patientrepo.save(patient);

        // Reset the form
        model.addAttribute("patient", new Patient());
        return "redirect:/index";
    }



}
