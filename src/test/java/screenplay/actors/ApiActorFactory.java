package screenplay.actors;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

public class ApiActorFactory {

    public static Actor createApiActor(String actorName) {
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        String baseUrl = environmentVariables.getProperty("restapi.baseURI", "http://localhost:3001/");
        return Actor.named(actorName).whoCan(CallAnApi.at(baseUrl));
    }
}
