package tn.pi.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.entity.Appointment;
import tn.pi.repository.AppointmentRepository;
import tn.pi.repository.DoctorRepository;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository; // Inject the DoctorRepository

    // Show the appointment form
    @GetMapping("/appointment")
    public String showAppointmentPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedInPatient") == null) {
            return "redirect:/login"; // Redirect to login if the patient is not logged in
        }

        // Add a new Appointment object and list of doctors to the model
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorRepository.findAll()); // Fetch all doctors from the database

        return "client/app/addAPP"; // Return the appointment form view
    }

    // Show all appointments
    @GetMapping("/appointments")
    public String showAppointments(Model model) {
        // Fetch appointments from the database and pass them to the model
        model.addAttribute("appointments", appointmentRepository.findAll());
        return "client/app/Appointments"; // Make sure this view exists in your templates
    }

    // Process the form and save the appointment
    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute Appointment appointment, HttpSession session, Model model) {
        // Check if the patient is logged in
        if (session.getAttribute("loggedInPatient") == null) {
            model.addAttribute("errorMessage", "You must be logged in to book an appointment.");
            return "client/app/addAPP";
        }

        try {
            // Save the appointment
            appointmentRepository.save(appointment);
            model.addAttribute("successMessage", "Your appointment was booked successfully!");

            // Redirect to the appointments page after success
            return "redirect:/appointments";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "There was an issue with your data. Please check and try again.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred while booking your appointment. Please try again.");
        }

        // Reload the doctors list in case of error
        model.addAttribute("doctors", doctorRepository.findAll());
        return "client/app/addAPP"; // Return to the appointment form page in case of error
    }
}
