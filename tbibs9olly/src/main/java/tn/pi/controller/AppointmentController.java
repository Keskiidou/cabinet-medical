package tn.pi.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.entity.Appointment;
import tn.pi.entity.Doctor;
import tn.pi.entity.Patient;
import tn.pi.entity.Schedule;
import tn.pi.repository.AppointmentRepository;
import tn.pi.repository.DoctorRepository;
import tn.pi.repository.ScheduleRepository;

import java.security.Principal;
import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;




    @GetMapping("/appointment")
    public String showAppointmentPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedInPatient") == null) {
            return "redirect:/login";
        }


        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorRepository.findAll());

        return "client/app/addAPP";
    }

    @GetMapping("/appointments")
    public String showAppointments(Model model, HttpSession session) {

        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");

        if (loggedInPatient == null) {
            model.addAttribute("errorMessage", "You must be logged in to view your appointments.");
            return "redirect:/login";
        }

        List<Appointment> appointments = appointmentRepository.findByPatient(loggedInPatient);

        model.addAttribute("appointments", appointments);
        return "client/app/Appointments";
    }



    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute Appointment appointment, HttpSession session, Model model) {

        // Check if the patient is logged in
        if (session.getAttribute("loggedInPatient") == null) {
            model.addAttribute("errorMessage", "You must be logged in to book an appointment.");
            return "client/app/addAPP";
        }

        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");

        if (loggedInPatient != null) {
            appointment.setPatient(loggedInPatient); // Set the logged-in patient for the appointment
        }

        // Retrieve the schedule and check availability
        if (appointment.getSchedule() != null && appointment.getSchedule().getId() != null) {
            Schedule schedule = scheduleRepository.findById(appointment.getSchedule().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));


            schedule.setAvailable(false);

            scheduleRepository.save(schedule);

            appointment.setSchedule(schedule);
        }

        // Save the appointment to the database
        appointmentRepository.save(appointment);

        // Add success message and redirect to the appointments page
        model.addAttribute("successMessage", "Your appointment was booked successfully!");
        return "redirect:/appointments";
    }
    private String getCurrentDoctorname(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor != null) {
            System.out.println("Doctor found: " + doctor.getName());
            return doctor.getName();
        }
        System.out.println("No doctor found in session");
        return null;
    }

    @GetMapping("/doctor/patient")  // Note: mapping is still “/doctor/patient”
    public String showPatientsAppointments(Model model, HttpSession session) {
        String doctorname = getCurrentDoctorname(session);
        if (doctorname == null) {
            return "redirect:/doctor/login";
        }
        List<Appointment> appointments = appointmentRepository.findByDoctor(doctorname);
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctorname", doctorname);
        // Optionally, add other details (doctorId, etc.) if you use them in the sidebar links.
        return "all-patients"; // This is the name of your template file (all-patients.html)
    }






}