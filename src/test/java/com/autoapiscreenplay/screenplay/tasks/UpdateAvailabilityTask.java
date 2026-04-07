package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.models.SetAvailabilityRequest;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class UpdateAvailabilityTask implements Task {

    private final boolean available;

    public UpdateAvailabilityTask(boolean available) {
        this.available = available;
    }

    public static UpdateAvailabilityTask to(boolean available) {
        return instrumented(UpdateAvailabilityTask.class, available);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall(ApiContextKeys.AUTH_TOKEN);
        SetAvailabilityRequest request = SetAvailabilityRequest.builder()
                .disponible(available)
                .build();

        actor.attemptsTo(
                Patch.to("medicos/disponibilidad")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .header("Authorization", "Bearer " + token)
                                .body(request))
        );
    }
}
