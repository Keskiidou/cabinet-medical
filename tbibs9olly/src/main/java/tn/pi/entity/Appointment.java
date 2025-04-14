package tn.pi.entity;

import jakarta.persistence.*;

import tn.pi.entity.Schedule;

@Entity
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

    // Default constructor
    public Appointment() {}

    // Constructor with parameters
    public Appointment(Long id, String patientName, String email, String phone, String doctor, String message, Schedule schedule, Patient patient) {
        this.id = id;
        this.patientName = patientName;
        this.email = email;
        this.phone = phone;
        this.doctor = doctor;
        this.message = message;
        this.schedule = schedule;
        this.patient = patient;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", doctor='" + doctor + '\'' +
                ", message='" + message + '\'' +
                ", schedule=" + schedule +
                ", patient=" + patient +
                '}';
    }
}
