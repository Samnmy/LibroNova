package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Member {
    // Private fields representing member attributes
    private Integer id;
    private String idNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate membershipDate;
    private Boolean active;
    private LocalDateTime createdAt;

    // Default constructor
    public Member() {}

    // Parameterized constructor for creating new members
    public Member(String idNumber, String firstName, String lastName,
                  String email, String phone) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.membershipDate = LocalDate.now(); // Set membership date to current date
        this.active = true; // New members are active by default
    }

    // Getter for member ID
    public Integer getId() { return id; }
    // Setter for member ID
    public void setId(Integer id) { this.id = id; }

    // Getter for identification number
    public String getIdNumber() { return idNumber; }
    // Setter for identification number
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    // Getter for first name
    public String getFirstName() { return firstName; }
    // Setter for first name
    public void setFirstName(String firstName) { this.firstName = firstName; }

    // Getter for last name
    public String getLastName() { return lastName; }
    // Setter for last name
    public void setLastName(String lastName) { this.lastName = lastName; }

    // Getter for email address
    public String getEmail() { return email; }
    // Setter for email address
    public void setEmail(String email) { this.email = email; }

    // Getter for phone number
    public String getPhone() { return phone; }
    // Setter for phone number
    public void setPhone(String phone) { this.phone = phone; }

    // Getter for membership start date
    public LocalDate getMembershipDate() { return membershipDate; }
    // Setter for membership start date
    public void setMembershipDate(LocalDate membershipDate) { this.membershipDate = membershipDate; }

    // Getter for active status
    public Boolean getActive() { return active; }
    // Setter for active status
    public void setActive(Boolean active) { this.active = active; }

    // Getter for creation timestamp
    public LocalDateTime getCreatedAt() { return createdAt; }
    // Setter for creation timestamp
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Convenience method to get full name (first name + last name)
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Overridden toString method for displaying member information
    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s %s, Active: %s",
                idNumber, firstName, lastName, active ? "Yes" : "No");
    }
}