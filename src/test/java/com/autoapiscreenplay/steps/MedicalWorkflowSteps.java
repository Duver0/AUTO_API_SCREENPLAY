package com.autoapiscreenplay.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import com.autoapiscreenplay.screenplay.actors.ApiActorFactory;
import com.autoapiscreenplay.screenplay.questions.ResponseBodyQuestion;
import com.autoapiscreenplay.screenplay.questions.ResponseStatusQuestion;
import com.autoapiscreenplay.screenplay.tasks.*;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class MedicalWorkflowSteps {

    private Actor apiConsumer;
    private String accountEmail;
    private final String accountPassword = "SecurePass123*";
    private String appointmentName;
    private int appointmentCedula;
    private String appointmentPriority;
    private String appointmentId;

    @Before
    public void setUpActor() {
        apiConsumer = ApiActorFactory.createApiActor("API Consumer");
    }

    @Given("an api consumer is authenticated to call the services")
    public void anApiConsumerIsAuthenticatedToCallTheServices() {
    }

    @When("the consumer creates a new internal account")
    public void theConsumerCreatesANewInternalAccount() {
        accountEmail = "qa.user.medical+" + System.currentTimeMillis() + "@example.com";
        apiConsumer.attemptsTo(CreateAccountTask.withData(accountEmail, accountPassword));
    }

    @When("the consumer signs in with valid credentials")
    public void theConsumerSignsInWithValidCredentials() {
        apiConsumer.attemptsTo(SignInTask.withCredentials(accountEmail, accountPassword));
    }

    @When("the consumer creates a new appointment")
    public void theConsumerCreatesANewAppointment() {
        appointmentName = "Paciente QA " + System.currentTimeMillis();
        appointmentCedula = (int) (System.currentTimeMillis() % 1_000_000_000L);
        appointmentPriority = "alta";

        apiConsumer.attemptsTo(CreateAppointmentTask.withData(appointmentName, appointmentCedula, appointmentPriority));
    }

    @When("the consumer consults the appointment queue")
    public void theConsumerConsultsTheAppointmentQueue() {
        apiConsumer.attemptsTo(GetAppointmentQueueTask.fromOperationsDashboard());
        apiConsumer.should(
                seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(200)),
                seeThat(ResponseBodyQuestion.field("find { it.cedula == " + appointmentCedula + " }.id"), notNullValue())
        );

        appointmentId = LastResponse.received().answeredBy(apiConsumer)
                .jsonPath()
                .getString("find { it.cedula == " + appointmentCedula + " }.id");
    }

    @When("the consumer updates the created appointment")
    public void theConsumerUpdatesTheCreatedAppointment() {
        apiConsumer.attemptsTo(UpdateAppointmentTask.withId(appointmentId));
    }

    @When("the consumer deletes the created appointment")
    public void theConsumerDeletesTheCreatedAppointment() {
        apiConsumer.attemptsTo(DeleteAppointmentTask.withId(appointmentId));
    }

    @Then("the operation should return status {int}")
    public void theOperationShouldReturnStatus(int statusCode) {
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(statusCode)));
    }
}
