package screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class GetAllResourcesTask implements Task {

    public static GetAllResourcesTask fromCatalog() {
        return new GetAllResourcesTask();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/products")
        );
    }
}
