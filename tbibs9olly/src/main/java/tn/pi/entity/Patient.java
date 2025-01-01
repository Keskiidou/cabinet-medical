package tn.pi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mandatory fields during registration
    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50)
    private String name;

    @NotNull(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Gender is mandatory")
    private String gender;

    // Corrected field name to lowercase "password"
    @Column(name = "password")
    private String password;  // Change to lowercase 'password'

    private String phone;
    private String address;
    private String symptoms;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
