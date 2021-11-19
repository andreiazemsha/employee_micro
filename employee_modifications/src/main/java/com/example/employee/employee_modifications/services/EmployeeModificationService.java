package com.example.employee.employee_modifications.services;

import com.example.employee.employee_modifications.dtos.EmployeeMessageDTO;
import com.example.employee.employee_modifications.entities.EmployeeStates;
import com.example.employee.employee_modifications.repositories.EmployeeRepository;
import com.example.employee.employee_modifications.entities.Employee;
import com.example.employee.employee_modifications.entities.EmployeeEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmployeeModificationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StateMachineFactory<EmployeeStates, EmployeeEvents> stateMachineFactory;

    @Autowired
    private EmployeeStateChangeInterceptor employeeStateChangeInterceptor;

    public final static String EMPLOYEE_ID_HEADER = "employee_id";


    @KafkaListener(topics = "${spring.kafka.topic.employee.create}")
    @SendTo
    public Employee processEmployeeCreateMessage(EmployeeMessageDTO message) {
        return employeeRepository.save(
                new Employee(
                        message.getName(),
                        message.getAge(),
                        message.getEmail())
        );
    }

    @KafkaListener(topics = "${spring.kafka.topic.employee.change-state}")
    @SendTo
    @Transactional
    public Object processEmployeeStateChangeMessage(EmployeeMessageDTO message) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(message.getId());
        if (optionalEmployee.isEmpty()) return null;
        Employee employee = optionalEmployee.get();
        StateMachine<EmployeeStates, EmployeeEvents> stateMachine = build(employee);
        if (sendEvent(message.getId(), stateMachine, message.getEvent())) {
            Optional<Employee> optionalUpdatedEmployee = employeeRepository.findById(message.getId());
            return optionalUpdatedEmployee.orElse(null);
        }
        return KafkaNull.INSTANCE;
    }

    private boolean sendEvent(Long employeeId, StateMachine<EmployeeStates, EmployeeEvents> stateMachine, EmployeeEvents employeeEvent) {
        Message<EmployeeEvents> message = MessageBuilder.withPayload(employeeEvent)
                .setHeader(EMPLOYEE_ID_HEADER, employeeId)
                .build();
        return stateMachine.sendEvent(message);
    }

    private StateMachine<EmployeeStates, EmployeeEvents> build(Employee employee) {
        String employeeIdKey = Long.toString(employee.getId());
        StateMachine<EmployeeStates, EmployeeEvents> stateMachine = stateMachineFactory.getStateMachine(employeeIdKey);

        stateMachine.stop();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(stateMachineAccessor -> {
                    stateMachineAccessor.resetStateMachine(new DefaultStateMachineContext<>(employee.getState(), null, null, null));
                    stateMachineAccessor.addStateMachineInterceptor(employeeStateChangeInterceptor);
                });
        stateMachine.start();

        return stateMachine;
    }

}
