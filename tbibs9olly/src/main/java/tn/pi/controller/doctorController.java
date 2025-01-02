package tn.pi.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.pi.entity.Doctor;
import tn.pi.entity.Patient;
import tn.pi.repository.DoctorRepository;
import tn.pi.repository.PatientRepository;

@Controller
public class doctorController {

    private final DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientrepo;

    public doctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctor/patient")
    public String patientList(
            Model model,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        int pageSize = 10; // Number of patients per page
        Page<Patient> patientsPage = patientrepo.findByNameContainingIgnoreCase(
                keyword, PageRequest.of(page, pageSize)
        );

        model.addAttribute("patients", patientsPage.getContent());
        model.addAttribute("pages", new int[patientsPage.getTotalPages()]);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "all-patients";
    }

    @GetMapping("/doctor/login")
    public String showLoginForm(Model model) {
        return "doctor_login"; // Show the login form
    }

    @PostMapping("/doctor/login")
    public String loginDoctor(@RequestParam String name, @RequestParam String accessCode, Model model, HttpSession session) {
        Doctor doctor = doctorRepository.findByName(name);

        if (doctor != null && doctor.getAccessCode().equals(accessCode)) {

            session.setAttribute("doctor", doctor);
            return "redirect:/doctor/dashboard"; // Redirect to dashboard after login
        } else {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "doctor_login";
        }
    }

    private Long getCurrentDoctorId(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            return doctor.getId();
        }
        return null;
    }
    // Dashboard for logged-in doctor
    @GetMapping("/doctor/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Long doctorId = getCurrentDoctorId(session);  // Get the current doctor's ID
        if (doctorId != null) {
            model.addAttribute("doctorId", doctorId);
            return "doctor_dashboard";  // Name of the Thymeleaf template
        } else {
            return "redirect:/doctor/login";  // If no doctor is found in the session, redirect to login
        }
    }

    // Logout doctor and clear session
    @GetMapping("/doctor/logout")
    public String logoutDoctor(HttpSession session) {
        session.invalidate(); // Invalidate the session to log the doctor out
        return "redirect:/doctor/login"; // Redirect to login page
    }
}
