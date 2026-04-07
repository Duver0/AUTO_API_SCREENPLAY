package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetConsultorioStateTask implements Task {

    private final String consultorioId;

    public GetConsultorioStateTask(String consultorioId) {
        this.consultorioId = consultorioId;
    }

    public static GetConsultorioStateTask forConsultorio(String consultorioId) {
        return instrumented(GetConsultorioStateTask.class, consultorioId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall(ApiContextKeys.AUTH_TOKEN);

        actor.attemptsTo(
                Get.resource("medicos/consultorio/estado/{consultorioId}")
                        .with(specification -> specification
                                .header("Authorization", "Bearer " + token)
                                .pathParam("consultorioId", consultorioId))
        );
    }
}
