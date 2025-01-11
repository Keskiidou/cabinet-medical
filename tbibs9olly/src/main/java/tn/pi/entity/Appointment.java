package tn.pi.entity;

import jakarta.persistence.*;
import lombok.*;

import tn.pi.entity.Schedule;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientName;
    private String email;
    private String phone;
    private String doctor;
    @Column(length = 500)
    private String message;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name="patient_id")
    private Patient patient;


}
