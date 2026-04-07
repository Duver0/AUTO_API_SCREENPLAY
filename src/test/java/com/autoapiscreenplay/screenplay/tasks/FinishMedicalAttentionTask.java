package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class FinishMedicalAttentionTask implements Task {

    public static FinishMedicalAttentionTask current() {
        return instrumented(FinishMedicalAttentionTask.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall(ApiContextKeys.AUTH_TOKEN);

        actor.attemptsTo(
                Post.to("medicos/atencion/finalizar")
                        .with(specification -> specification
                                .header("Authorization", "Bearer " + token))
        );
    }
}
