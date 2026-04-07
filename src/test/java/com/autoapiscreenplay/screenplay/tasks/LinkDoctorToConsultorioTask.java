package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.models.AssignConsultorioRequest;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class LinkDoctorToConsultorioTask implements Task {

    private final String consultorioId;

    public LinkDoctorToConsultorioTask(String consultorioId) {
        this.consultorioId = consultorioId;
    }

    public static LinkDoctorToConsultorioTask forConsultorio(String consultorioId) {
        return instrumented(LinkDoctorToConsultorioTask.class, consultorioId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall(ApiContextKeys.AUTH_TOKEN);
        AssignConsultorioRequest request = AssignConsultorioRequest.builder()
                .consultorioId(consultorioId)
                .build();

        actor.attemptsTo(
                Post.to("medicos/consultorio/asignar")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(request))
        );
    }
}
