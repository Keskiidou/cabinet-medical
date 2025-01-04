package tn.pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private int dayOfMonth;

    // Storing month (1-12)
    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    // Getter and Setter for DayOfMonth
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    // Getter and Setter for Month
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    // Getter and Setter for StartTime
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    // Getter and Setter for EndTime
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    // Getter and Setter for Available
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Getter for Doctor ID
    public Long getDoctorId() {
        return this.doctor != null ? this.doctor.getId() : null;
    }

    public void setDoctorId(Long doctorId) {
        this.doctor = new Doctor(doctorId);
    }

    // Getter and Setter for Doctor
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
