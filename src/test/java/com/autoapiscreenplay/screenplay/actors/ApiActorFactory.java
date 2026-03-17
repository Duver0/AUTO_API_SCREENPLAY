package com.autoapiscreenplay.screenplay.actors;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.util.EnvironmentVariables;
import net.thucydides.model.environment.SystemEnvironmentVariables;

public class ApiActorFactory {

    public static Actor createApiActor(String actorName) {
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        String baseUrl = environmentVariables.getProperty("base.url", "http://localhost:3000/");
        return Actor.named(actorName).whoCan(CallAnApi.at(baseUrl));
    }
}
