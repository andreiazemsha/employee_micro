package com.example.employee.employee_modifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.config.EnableStateMachine;

@SpringBootApplication
@EnableStateMachine
public class EmployeeModificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeModificationsApplication.class, args);
    }
}
