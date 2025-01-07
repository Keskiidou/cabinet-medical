package tn.pi.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tn.pi.entity.Patient;
import tn.pi.repository.PatientRepository;

import java.util.Optional;

@Controller
public class PatientController {

    private final PatientRepository patientRepo;

    // Constructor injection for the repository
    public PatientController(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        if (session.getAttribute("loggedInPatient") != null) {
            model.addAttribute("loggedInPatient", session.getAttribute("loggedInPatient"));
            return "redirect:/client/index"; // Redirect to client index if logged in
        }
        return "client/index"; // Show the index page if not logged in
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedInPatient") != null) {
            model.addAttribute("loggedInPatient", session.getAttribute("loggedInPatient"));
            return "redirect:/client/index"; // Redirect to client index if already logged in
        }
        model.addAttribute("patient", new Patient());
        return "client/patient/login";
    }

    @PostMapping("/login")
    public String loginPatient(@Valid Patient patient, BindingResult bindingResult, HttpSession session, Model model) {
        if (patient.getEmail() == null || patient.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "error.patient", "Email cannot be empty");
            return "client/patient/login";
        }

        Optional<Patient> existingPatientOptional = patientRepo.findByEmailIgnoreCase(patient.getEmail());

        if (existingPatientOptional.isEmpty() || !existingPatientOptional.get().getPassword().equals(patient.getPassword())) {
            bindingResult.rejectValue("email", "error.patient", "Invalid email or password");
            return "client/patient/login";
        }

        // Store logged-in patient in session
        Patient existingPatient = existingPatientOptional.get();
        session.setAttribute("loggedInPatient", existingPatient);

        return "redirect:/client/index";
    }

    @GetMapping("/client/index")
    public String showClientIndex(HttpSession session, Model model) {
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        if (loggedInPatient != null) {
            model.addAttribute("loggedInPatient", loggedInPatient);  // Add logged-in patient to model
        }
        return "client/index"; // Show the client index page
    }

    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");

        if (loggedInPatient == null) {
            return "redirect:/login";  // Redirect to login page if not logged in
        }

        model.addAttribute("patient", loggedInPatient);
        model.addAttribute("successMessage", false);  // Default no success message
        return "client/patient/profile";  // Thymeleaf template
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid Patient patient, BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patient", patient);
            return "client/patient/profile";  // Return to the profile page if errors
        }

        // Log the submitted patient data to verify the changes
        System.out.println("Submitted Patient Data: " + patient);

        // Retrieve the logged-in patient from the session
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");

        if (loggedInPatient != null) {
            // Log the current data before updating
            System.out.println("Current Patient Data: " + loggedInPatient);

            // Update the logged-in patient's details
            loggedInPatient.setName(patient.getName());
            loggedInPatient.setPassword(patient.getPassword());
            loggedInPatient.setPhone(patient.getPhone());
            loggedInPatient.setAddress(patient.getAddress());

            // Log the updated data
            System.out.println("Updated Patient Data: " + loggedInPatient);

            // Save the updated patient to the database
            patientRepo.save(loggedInPatient);

            // Update the session with the new patient details
            session.setAttribute("loggedInPatient", loggedInPatient);

            // Add a success message to be displayed
            model.addAttribute("successMessage", true);
        }

        model.addAttribute("patient", loggedInPatient);  // Send updated patient back to view
        return "client/patient/profile";  // Reload the profile page
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/index"; // Redirect to home page after logout
    }
    @GetMapping("/register")
    public String showRegistrationForm(HttpSession session, Model model) {
        // Check if the user is already logged in
        if (session.getAttribute("loggedInPatient") != null) {
            // Redirect to the home page if already logged in
            return "redirect:/index";
        }

        // If not logged in, show the registration form
        model.addAttribute("patient", new Patient());
        return "client/patient/register";
    }
    @PostMapping("/register")
    public String registerPatient(@ModelAttribute("patient") Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "client/patient/register";
        }

        try {
            // Save the patient to the database
            patientRepo.save(patient);

            // Redirect to the index page on successful registration
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred during registration. Please try again.");
            return "client/patient/register";
        }
    }



    @GetMapping("/doctor")
    public String showDoctorPage(HttpSession session) {
        if (session.getAttribute("loggedInPatient") == null) {
            return "redirect:/login"; // Redirect to login page if not logged in
        }
        return "client/doctor/doctor"; // Show doctor page if logged in
    }


}
