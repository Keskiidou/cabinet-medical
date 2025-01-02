package tn.pi.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialization;
    private String email;
    private String phone;
    private String address;
    private String accessCode;

    public Doctor(Long id) {
        this.id = id;
    }
    public String getAccessCode() {
        return this.accessCode;
    }
    public Long getId() {
        return id;
    }

    public Doctor() {
    }
}
