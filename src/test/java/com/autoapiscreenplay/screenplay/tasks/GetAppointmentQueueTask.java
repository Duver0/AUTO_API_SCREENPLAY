package com.autoapiscreenplay.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class GetAppointmentQueueTask implements Task {

    public static GetAppointmentQueueTask fromOperationsDashboard() {
        return new GetAppointmentQueueTask();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource("turnos"));
    }
}
