package com.autoapiscreenplay.screenplay.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;
import com.autoapiscreenplay.screenplay.models.AppointmentRequest;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class UpdateAppointmentTask implements Task {

    private final String appointmentId;

    public UpdateAppointmentTask(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public static UpdateAppointmentTask withId(String appointmentId) {
        return instrumented(UpdateAppointmentTask.class, appointmentId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        AppointmentRequest request = AppointmentRequest.builder()
                .nombre("Paciente QA Actualizado")
                .cedula(123456789)
                .priority("baja")
                .build();

        actor.attemptsTo(
            Put.to("turnos/" + appointmentId)
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .body(request))
        );
    }
}
