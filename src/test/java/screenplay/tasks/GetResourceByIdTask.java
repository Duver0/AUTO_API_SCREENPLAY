package screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class GetResourceByIdTask implements Task {

    private final int productId;

    public GetResourceByIdTask(int productId) {
        this.productId = productId;
    }

    public static GetResourceByIdTask forProduct(int id) {
        return new GetResourceByIdTask(id);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/products/{id}")
                        .with(requestSpec -> requestSpec
                                .pathParam("id", productId))
        );
    }
}
