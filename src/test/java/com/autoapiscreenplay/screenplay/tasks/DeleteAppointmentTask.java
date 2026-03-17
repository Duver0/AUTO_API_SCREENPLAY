package com.autoapiscreenplay.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteAppointmentTask implements Task {

    private final String appointmentId;

    public DeleteAppointmentTask(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public static DeleteAppointmentTask withId(String appointmentId) {
        return instrumented(DeleteAppointmentTask.class, appointmentId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Delete.from("turnos/" + appointmentId)
        );
    }
}
