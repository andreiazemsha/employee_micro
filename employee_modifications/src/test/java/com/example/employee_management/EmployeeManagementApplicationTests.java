package com.example.employee_management;

import com.example.employee.employee_modifications.EmployeeModificationsApplication;
import com.example.employee.employee_modifications.entities.EmployeeEvents;
import com.example.employee.employee_modifications.entities.EmployeeStates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = EmployeeModificationsApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeModificationsApplicationTests {

    @Value("${local.server.port}")
    private int ports;

    @Autowired
    private StateMachineFactory<EmployeeStates, EmployeeEvents> factory;

    @Test
    public void testStateMachineTransitions() throws Exception {
        StateMachine<EmployeeStates, EmployeeEvents> machine = factory.getStateMachine();
        StateMachineTestPlan<EmployeeStates, EmployeeEvents> plan = StateMachineTestPlanBuilder.<EmployeeStates, EmployeeEvents> builder()
                        .defaultAwaitTime(2)
                        .stateMachine(machine)
                        .step()
                        .expectStates(EmployeeStates.ADDED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(EmployeeEvents.REGISTER)
                        .expectState(EmployeeStates.IN_CHECK)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(EmployeeEvents.APPROVE)
                        .expectState(EmployeeStates.APPROVED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(EmployeeEvents.ACTIVATE)
                        .expectState(EmployeeStates.ACTIVE)
                        .expectStateChanged(1)
                        .and()
                        .build();
        plan.test();
    }

}
