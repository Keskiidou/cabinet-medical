package tn.pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    // Getter and Setter for ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for DayOfWeek
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    // Setter for Doctor ID (if you need to set doctorId directly)
    public void setDoctorId(Long doctorId) {
        if (this.doctor == null) {
            this.doctor = new Doctor();
        }
        this.doctor.setId(doctorId);
    }

    // Getter and Setter for Doctor
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
