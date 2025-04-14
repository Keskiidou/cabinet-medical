    package tn.pi.entity;
    
    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
    
    @Entity
    public class Patient {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        @NotNull(message = "Name is mandatory")
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        @Column(nullable = false, length = 50)
        private String name;
    
        @NotNull(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        @Column(nullable = false, unique = true)
        private String email;
    
        @NotNull(message = "Gender is mandatory")
        @Column(nullable = false, length = 10)
        private String gender;
    
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Column(nullable = false)
        private String password;
    
        @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must be valid")
        @Column(length = 15)
        private String phone;
    
        @Column(length = 100)
        private String address;
    
        @Column(length = 255)
        private String symptoms;
    
        // Default constructor
        public Patient() {}
    
        // Constructor with parameters
        public Patient(Long id, String name, String email, String gender, String password, String phone, String address, String symptoms) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.gender = gender;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.symptoms = symptoms;
        }
    
        // Getter and Setter methods
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    
        public String getGender() {
            return gender;
        }
    
        public void setGender(String gender) {
            this.gender = gender;
        }
    
        public String getPassword() {
            return password;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    
        public String getPhone() {
            return phone;
        }
    
        public void setPhone(String phone) {
            this.phone = phone;
        }
    
        public String getAddress() {
            return address;
        }
    
        public void setAddress(String address) {
            this.address = address;
        }
    
        public String getSymptoms() {
            return symptoms;
        }
    
        public void setSymptoms(String symptoms) {
            this.symptoms = symptoms;
        }
    
        @Override
        public String toString() {
            return "Patient{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", gender='" + gender + '\'' +
                    ", password='" + password + '\'' +
                    ", phone='" + phone + '\'' +
                    ", address='" + address + '\'' +
                    ", symptoms='" + symptoms + '\'' +
                    '}';
        }
    }
