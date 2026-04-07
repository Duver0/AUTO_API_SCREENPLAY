package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.models.StartAttentionRequest;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class StartMedicalAttentionTask implements Task {

    public static StartMedicalAttentionTask withRememberedPatient() {
        return instrumented(StartMedicalAttentionTask.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall(ApiContextKeys.AUTH_TOKEN);
        String patientName = actor.recall(ApiContextKeys.PATIENT_NAME);
        String patientDocument = actor.recall(ApiContextKeys.PATIENT_DOCUMENT);

        StartAttentionRequest request = StartAttentionRequest.builder()
                .pacienteNombre(patientName)
                .pacienteDocumento(patientDocument)
                .build();

        actor.attemptsTo(
                Post.to("medicos/atencion/iniciar")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(request))
        );
    }
}
