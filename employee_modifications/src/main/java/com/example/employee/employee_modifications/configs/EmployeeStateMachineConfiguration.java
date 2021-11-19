package com.example.employee.employee_modifications.configs;

import com.example.employee.employee_modifications.entities.EmployeeEvents;
import com.example.employee.employee_modifications.entities.EmployeeStates;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class EmployeeStateMachineConfiguration extends StateMachineConfigurerAdapter<EmployeeStates, EmployeeEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeStates, EmployeeEvents> config) throws Exception {
        config.withConfiguration()
                .autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeStates, EmployeeEvents> states) throws Exception {
        states.withStates()
                .initial(EmployeeStates.ADDED)
                .state(EmployeeStates.IN_CHECK)
                .state(EmployeeStates.APPROVED)
                .end(EmployeeStates.ACTIVE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeStates, EmployeeEvents> transitions) throws Exception {
        transitions
                .withExternal().source(EmployeeStates.ADDED).target(EmployeeStates.IN_CHECK).event(EmployeeEvents.REGISTER)
                .and()
                .withExternal().source(EmployeeStates.IN_CHECK).target(EmployeeStates.APPROVED).event(EmployeeEvents.APPROVE)
                .and()
                .withExternal().source(EmployeeStates.APPROVED).target(EmployeeStates.ACTIVE).event(EmployeeEvents.ACTIVATE);
    }
}