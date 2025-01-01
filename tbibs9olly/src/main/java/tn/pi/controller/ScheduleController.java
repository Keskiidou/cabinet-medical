package tn.pi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.entity.Doctor;
import tn.pi.entity.Schedule;
import tn.pi.reposotry.ScheduleRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/doctor/{doctorId}")
    public String listSchedulesForDoctor(@PathVariable Long doctorId, Model model) {
        // Get all schedules for the doctor
        List<Schedule> schedules = scheduleRepository.findByDoctorId(doctorId);
        model.addAttribute("schedules", schedules);
        return "doctor_schedule_list"; // Show all schedules for the doctor
    }

    @PostMapping("/add")
    public String addSchedule(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("dayOfWeek") DayOfWeek dayOfWeek,
            @RequestParam("startTime") LocalTime startTime,
            Model model
    ) {
        LocalTime endTime = startTime.plusHours(2); // Enforce 2-hour slots

        // Check for overlapping schedules
        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(doctorId, dayOfWeek, startTime, endTime);

        if (!overlappingSchedules.isEmpty()) {
            model.addAttribute("error", "The schedule overlaps with an existing slot.");
            return "doctor_schedule_form"; // Return to the form with an error
        }

        // Save the new schedule
        Schedule schedule = new Schedule();
        schedule.setDoctor(new Doctor(doctorId)); // Link to the doctor
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        scheduleRepository.save(schedule);

        return "redirect:/doctor/schedules/" + doctorId;
    }
}
