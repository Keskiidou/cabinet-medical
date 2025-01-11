package tn.pi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.entity.Doctor;
import tn.pi.entity.Schedule;
import tn.pi.repository.DoctorRepository;
import tn.pi.repository.ScheduleRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/doctor/schedules")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public ScheduleController(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctor/{doctorId}")
    public String listSchedulesForDoctor(@PathVariable Long doctorId, Model model) {
        List<Schedule> schedules = scheduleRepository.findByDoctor_Id(doctorId);
        model.addAttribute("schedules", schedules);
        model.addAttribute("doctorId", doctorId);

        return "doctor_schedule_list";
    }

    @GetMapping("/add/{doctorId}")
    public String showAddScheduleForm(@PathVariable Long doctorId, Model model) {
        // Check if doctor exists
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            model.addAttribute("error", "Doctor not found.");
            return "redirect:/doctor/dashboard";  // Redirect to the dashboard or login if doctor not found
        }

        System.out.println("Doctor ID in showAddScheduleForm: " + doctorId);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("doctors", doctorRepository.findAll());
        return "add_schedule"; // Form to add schedule
    }



    @PostMapping("/add/{doctorId}")
    public String addSchedule(
            @PathVariable Long doctorId,
            @RequestParam("startTime") LocalTime startTime,
            @RequestParam("dayOfMonth") int dayOfMonth,
            @RequestParam("month") int month,
            Model model
    ) {
        LocalTime endTime = startTime.plusHours(2);
        boolean isAvailable = true;

        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(doctorId, dayOfMonth, month, startTime, endTime);
        if (!overlappingSchedules.isEmpty()) {
            model.addAttribute("error", "The schedule overlaps with an existing slot.");
            return "add_schedule"; // Return to the form with an error message
        }


        Schedule schedule = new Schedule();
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor != null) {
            schedule.setDoctorId(doctorId);
            schedule.setDayOfMonth(dayOfMonth);
            schedule.setMonth(month);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setAvailable(isAvailable);
            scheduleRepository.save(schedule);
        } else {
            model.addAttribute("error", "Doctor not found.");
            return "doctor_dashboard"; // Return to the form with an error
        }

        return "redirect:/doctor/schedules/doctor/" + doctorId;
    }


}
