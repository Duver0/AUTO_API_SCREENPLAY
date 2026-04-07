package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.models.AppointmentRequest;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreatePatientInQueueTask implements Task {

    private final String patientName;
    private final String patientDocument;

    public CreatePatientInQueueTask(String patientName, String patientDocument) {
        this.patientName = patientName;
        this.patientDocument = patientDocument;
    }

    public static CreatePatientInQueueTask withData(String patientName, String patientDocument) {
        return instrumented(CreatePatientInQueueTask.class, patientName, patientDocument);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.remember(ApiContextKeys.PATIENT_NAME, patientName);
        actor.remember(ApiContextKeys.PATIENT_DOCUMENT, patientDocument);

        AppointmentRequest request = AppointmentRequest.builder()
                .nombre(patientName)
                .cedula(Integer.parseInt(patientDocument))
                .priority("alta")
                .build();

        actor.attemptsTo(
                Post.to("turnos")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .body(request))
        );
    }
}
