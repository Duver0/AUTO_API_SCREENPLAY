package com.autoapiscreenplay.screenplay.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import com.autoapiscreenplay.screenplay.models.AppointmentRequest;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateAppointmentTask implements Task {

    private final String nombre;
    private final int cedula;
    private final String priority;

    public CreateAppointmentTask(String nombre, int cedula, String priority) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.priority = priority;
    }

    public static CreateAppointmentTask withData(String nombre, int cedula, String priority) {
        return instrumented(CreateAppointmentTask.class, nombre, cedula, priority);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        AppointmentRequest request = AppointmentRequest.builder()
                .nombre(nombre)
                .cedula(cedula)
                .priority(priority)
                .build();

        actor.attemptsTo(
            Post.to("turnos")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .body(request))
        );
    }
}
