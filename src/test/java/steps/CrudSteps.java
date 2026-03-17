package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import screenplay.actors.ApiActorFactory;
import screenplay.models.ProductRequest;
import screenplay.questions.ResponseBodyQuestion;
import screenplay.questions.ResponseStatusQuestion;
import screenplay.tasks.CreateAnotherResourceTask;
import screenplay.tasks.CreateResourceTask;
import screenplay.tasks.GetAllResourcesTask;
import screenplay.tasks.GetResourceByIdTask;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class CrudSteps {

    private final Actor apiActor = ApiActorFactory.createApiActor("API Consumer");
    private int secondProductId;

    @Given("a product has been registered in the catalog")
    public void aProductHasBeenRegisteredInTheCatalog() {
        ProductRequest firstProduct = ProductRequest.builder()
                .name("Laptop Pro")
                .description("High performance laptop for professionals")
                .price(1299.99)
                .stock(50)
                .build();

        apiActor.attemptsTo(CreateResourceTask.with(firstProduct));
        apiActor.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(201)));
    }

    @When("the catalog is queried to confirm the product was added")
    public void theCatalogIsQueriedToConfirmTheProductWasAdded() {
        apiActor.attemptsTo(GetAllResourcesTask.fromCatalog());

        apiActor.should(
                seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(200)),
                seeThat(ResponseBodyQuestion.theBody(), containsString("Laptop Pro"))
        );
    }

    @And("a second product is registered with different specifications")
    public void aSecondProductIsRegisteredWithDifferentSpecifications() {
        ProductRequest secondProduct = ProductRequest.builder()
                .name("USB-C Hub")
                .description("Multi-port USB-C hub with power delivery")
                .price(49.99)
                .stock(200)
                .build();

        apiActor.attemptsTo(CreateAnotherResourceTask.with(secondProduct));
        apiActor.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(201)));
        secondProductId = SerenityRest.lastResponse().jsonPath().getInt("id");
    }

    @Then("the second product can be retrieved and its details validated by identifier")
    public void theSecondProductCanBeRetrievedAndItsDetailsValidatedByIdentifier() {
        apiActor.attemptsTo(GetResourceByIdTask.forProduct(secondProductId));

        apiActor.should(
                seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(200)),
                seeThat(ResponseBodyQuestion.theBody(), containsString("USB-C Hub"))
        );
    }
}
