package com.restauracja.restauracjaapi.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "waiters")
public class Waiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    private boolean active = true;

    public Waiter(){}

    public Waiter(String firstName,String lastName,LocalDate hireDate,boolean active){
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireDate = hireDate;
        this.active = active;
    }
    public Long getId() {
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public LocalDate getHireDate(){
        return hireDate;
    }
    public void setHireDate(LocalDate hireDate){
        this.hireDate = hireDate;
    }
    public boolean isActive(){
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }
}
