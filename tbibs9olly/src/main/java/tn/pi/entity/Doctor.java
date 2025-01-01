package tn.pi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
