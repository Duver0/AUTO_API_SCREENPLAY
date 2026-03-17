package screenplay.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import screenplay.models.ProductRequest;

public class CreateResourceTask implements Task {

    private final ProductRequest productRequest;

    public CreateResourceTask(ProductRequest productRequest) {
        this.productRequest = productRequest;
    }

    public static CreateResourceTask with(ProductRequest request) {
        return new CreateResourceTask(request);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/products")
                        .with(requestSpec -> requestSpec
                                .contentType(ContentType.JSON)
                                .body(productRequest))
        );
    }
}
