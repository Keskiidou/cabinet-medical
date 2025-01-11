package tn.pi.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.pi.entity.Doctor;
import tn.pi.entity.Patient;
import tn.pi.entity.Schedule;
import tn.pi.repository.DoctorRepository;
import tn.pi.repository.PatientRepository;
import tn.pi.repository.ScheduleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class doctorController {

    private final DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientrepo;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public doctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctor/{id}")
    public String getDoctorProfile(@PathVariable Long id, Model model) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) {
            return "redirect:/doctor/login";  // Redirect to login if the doctor is not found
        }

        model.addAttribute("doctor", doctor);  // Pass doctor object to the view
        return "doctor";  // Return to the doctor.html page
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
    private String getCurrentDoctorname(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            return doctor.getName();
        }
        return null;
    }
    private String getCurrentDoctoremail(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            return doctor.getEmail();
        }
        return null;
    }
    private String getCurrentDoctorphone(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            return doctor.getPhone();
        }
        return null;
    }
    private String getCurrentAddress(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            return doctor.getAddress();
        }
        return null;
    }
    private String getCurrentspec(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            return doctor.getSpecialization();
        }
        return null;
    }
    // Dashboard for logged-in doctor
    @GetMapping("/doctor/dashboard")

    public String dashboard(Model model, HttpSession session) {
        Long doctorId = getCurrentDoctorId(session);
        String doctorname = getCurrentDoctorname(session);
        String doctoremail = getCurrentDoctoremail(session);
        String doctorphone = getCurrentDoctorphone(session);
        String doctoraddress = getCurrentAddress(session);
        String doctorspecialization = getCurrentspec(session);
        if (doctorId != null) {
            model.addAttribute("doctorId", doctorId);
            model.addAttribute("doctorname", doctorname);
            model.addAttribute("doctoremail", doctoremail);
            model.addAttribute("doctorphone", doctorphone);
            model.addAttribute("doctoraddress", doctoraddress);
            model.addAttribute("doctorspecialization", doctorspecialization);
            return "doctor_dashboard";
        } else {
            return "redirect:/doctor/login";
        }
    }

    // Logout doctor and clear session
    @GetMapping("/doctor/logout")
    public String logoutDoctor(HttpSession session) {
        session.invalidate(); // Invalidate the session to log the doctor out
        return "redirect:/doctor/login"; // Redirect to login page
    }
    @GetMapping("/alldoctors")
    public String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();

        // Create a map to store doctor schedules
        Map<Long, List<Schedule>> doctorSchedulesMap = new HashMap<>();

        // Populate the map with doctor schedules
        for (Doctor doctor : doctors) {
            List<Schedule> schedules = scheduleRepository.findByDoctor_Id(doctor.getId());
            doctorSchedulesMap.put(doctor.getId(), schedules);
        }

        // Add the doctors list and the map of schedules to the model
        model.addAttribute("doctors", doctors);
        model.addAttribute("doctorSchedulesMap", doctorSchedulesMap);

        return "client/doctor/doctor-list";
    }

}
