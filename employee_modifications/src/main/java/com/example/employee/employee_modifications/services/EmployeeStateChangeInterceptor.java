package com.example.employee.employee_modifications.services;

import com.example.employee.employee_modifications.repositories.EmployeeRepository;
import com.example.employee.employee_modifications.entities.EmployeeEvents;
import com.example.employee.employee_modifications.entities.EmployeeStates;
import com.example.employee.employee_modifications.entities.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;


@RequiredArgsConstructor
@Component
public class EmployeeStateChangeInterceptor extends StateMachineInterceptorAdapter<EmployeeStates, EmployeeEvents> {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void preStateChange(State<EmployeeStates, EmployeeEvents> state, Message<EmployeeEvents> message, Transition<EmployeeStates, EmployeeEvents> transition, StateMachine<EmployeeStates, EmployeeEvents> stateMachine, StateMachine<EmployeeStates, EmployeeEvents> rootStateMachine) {
        Optional.ofNullable(message).flatMap(existingMessage -> Optional.ofNullable(
                (Long)existingMessage.getHeaders().get(EmployeeModificationService.EMPLOYEE_ID_HEADER)))
                .ifPresent(employeeId -> {
                    Employee employee = employeeRepository.getById(employeeId);
                    employee.setState(state.getId());
                    employeeRepository.save(employee);
                });
    }
}
