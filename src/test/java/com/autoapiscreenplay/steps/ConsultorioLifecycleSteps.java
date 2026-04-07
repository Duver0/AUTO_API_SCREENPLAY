package com.autoapiscreenplay.steps;

import com.autoapiscreenplay.screenplay.actors.ApiActorFactory;
import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.interactions.ResolveAvailableConsultorioInteraction;
import com.autoapiscreenplay.screenplay.interactions.WaitForConsultorioStateInteraction;
import com.autoapiscreenplay.screenplay.questions.ResponseBodyQuestion;
import com.autoapiscreenplay.screenplay.questions.ResponseStatusQuestion;
import com.autoapiscreenplay.screenplay.tasks.AuthenticateDoctorTask;
import com.autoapiscreenplay.screenplay.tasks.CreatePatientInQueueTask;
import com.autoapiscreenplay.screenplay.tasks.FinishMedicalAttentionTask;
import com.autoapiscreenplay.screenplay.tasks.GetConsultorioStateTask;
import com.autoapiscreenplay.screenplay.tasks.LinkDoctorToConsultorioTask;
import com.autoapiscreenplay.screenplay.tasks.RegisterDoctorAccountTask;
import com.autoapiscreenplay.screenplay.tasks.ReleaseConsultorioTask;
import com.autoapiscreenplay.screenplay.tasks.StartMedicalAttentionTask;
import com.autoapiscreenplay.screenplay.tasks.UpdateAvailabilityTask;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ConsultorioLifecycleSteps {

    private Actor apiConsumer;

    @Before("@ciclo_vida_consultorio")
    public void setUpActor() {
        apiConsumer = ApiActorFactory.createApiActor("Consultorio API Consumer");
    }

    @After("@ciclo_vida_consultorio")
    public void cleanScenarioState() {
        if (apiConsumer == null) {
            return;
        }

        String email = apiConsumer.recall(ApiContextKeys.ACCOUNT_EMAIL);
        String password = apiConsumer.recall(ApiContextKeys.ACCOUNT_PASSWORD);
        String consultorioId = apiConsumer.recall(ApiContextKeys.CONSULTORIO_ID);
        String doctorId = apiConsumer.recall(ApiContextKeys.DOCTOR_ID);

        if (email == null || password == null || consultorioId == null || doctorId == null) {
            return;
        }

        try {
            apiConsumer.attemptsTo(AuthenticateDoctorTask.withCredentials(email, password));
            Integer signInStatus = ResponseStatusQuestion.theStatusCode().answeredBy(apiConsumer);
            if (signInStatus == null || signInStatus != 200) {
                return;
            }

            apiConsumer.attemptsTo(GetConsultorioStateTask.forConsultorio(consultorioId));
            Integer stateStatus = ResponseStatusQuestion.theStatusCode().answeredBy(apiConsumer);
            if (stateStatus == null || stateStatus != 200) {
                return;
            }

            String currentDoctorId = ResponseBodyQuestion.field("medicoId").answeredBy(apiConsumer);
            String currentState = ResponseBodyQuestion.field("estado").answeredBy(apiConsumer);

            if (currentDoctorId == null || !doctorId.equals(currentDoctorId)) {
                return;
            }

            if ("EnAtencion".equals(currentState)) {
                apiConsumer.attemptsTo(FinishMedicalAttentionTask.current());
                apiConsumer.attemptsTo(
                        WaitForConsultorioStateInteraction.toBe("ConMedicoDisponible")
                                .withoutVisiblePatient()
                                .withLinkedDoctor()
                );
            }

            if (!"SinMedico".equals(currentState)) {
                apiConsumer.attemptsTo(ReleaseConsultorioTask.current());
                apiConsumer.attemptsTo(
                        WaitForConsultorioStateInteraction.toBe("SinMedico")
                                .withoutVisiblePatient()
                                .withoutLinkedDoctor()
                );
            }
        } catch (RuntimeException ignored) {
        }
    }

    @Given("a doctor account is authenticated and an available consultorio is identified")
    public void aDoctorAccountIsAuthenticatedAndAnAvailableConsultorioIsIdentified() {
        authenticateDoctorFromScratch();
        apiConsumer.attemptsTo(ResolveAvailableConsultorioInteraction.fromConfiguredRange());
    }

    @Given("a doctor account is authenticated, linked, and has an active attention with a patient")
    public void aDoctorAccountIsAuthenticatedLinkedAndHasAnActiveAttentionWithAPatient() {
        prepareLinkedConsultorioFromScratch();
        createAndStartActiveAttention();
    }

    @Given("a doctor account is authenticated, linked, and has completed the active attention")
    public void aDoctorAccountIsAuthenticatedLinkedAndHasCompletedTheActiveAttention() {
        prepareLinkedConsultorioFromScratch();
        createAndStartActiveAttention();

        apiConsumer.attemptsTo(FinishMedicalAttentionTask.current());
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(202)));
        apiConsumer.attemptsTo(
                WaitForConsultorioStateInteraction.toBe("ConMedicoDisponible")
                        .withoutVisiblePatient()
                        .withLinkedDoctor()
        );
    }

    @Given("a doctor account is authenticated, linked, and the consultorio is non available")
    public void aDoctorAccountIsAuthenticatedLinkedAndTheConsultorioIsNonAvailable() {
        prepareLinkedConsultorioFromScratch();

        apiConsumer.attemptsTo(UpdateAvailabilityTask.to(false));
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(202)));
        apiConsumer.attemptsTo(
                WaitForConsultorioStateInteraction.toBe("ConMedicoNoDisponible")
                        .withoutVisiblePatient()
                        .withLinkedDoctor()
        );
    }

    @When("the doctor links to the selected consultorio")
    public void theDoctorLinksToTheSelectedConsultorio() {
        String consultorioId = apiConsumer.recall(ApiContextKeys.CONSULTORIO_ID);
        apiConsumer.attemptsTo(LinkDoctorToConsultorioTask.forConsultorio(consultorioId));
    }

    @When("the actor consults the selected consultorio state")
    public void theActorConsultsTheSelectedConsultorioState() {
        String consultorioId = apiConsumer.recall(ApiContextKeys.CONSULTORIO_ID);
        apiConsumer.attemptsTo(GetConsultorioStateTask.forConsultorio(consultorioId));
    }

    @When("the doctor updates consultorio availability to false")
    public void theDoctorUpdatesConsultorioAvailabilityToFalse() {
        apiConsumer.attemptsTo(UpdateAvailabilityTask.to(false));
    }

    @When("the doctor releases the selected consultorio")
    public void theDoctorReleasesTheSelectedConsultorio() {
        apiConsumer.attemptsTo(ReleaseConsultorioTask.current());
    }

    @Then("the last operation should return status {int}")
    public void theLastOperationShouldReturnStatus(int statusCode) {
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(statusCode)));
    }

    @Then("the consultorio state should become {string}")
    public void theConsultorioStateShouldBecome(String expectedState) {
        apiConsumer.attemptsTo(WaitForConsultorioStateInteraction.toBe(expectedState));
        apiConsumer.should(seeThat(ResponseBodyQuestion.field("estado"), equalTo(expectedState)));
    }

    @Then("the consultorio state should be {string}")
    public void theConsultorioStateShouldBe(String expectedState) {
        apiConsumer.should(seeThat(ResponseBodyQuestion.field("estado"), equalTo(expectedState)));
    }

    @And("the consultorio should be linked to the authenticated doctor")
    public void theConsultorioShouldBeLinkedToTheAuthenticatedDoctor() {
        String expectedDoctorId = apiConsumer.recall(ApiContextKeys.DOCTOR_ID);
        apiConsumer.should(seeThat(ResponseBodyQuestion.field("medicoId"), equalTo(expectedDoctorId)));
    }

    @And("the active patient should be visible in consultorio state")
    public void theActivePatientShouldBeVisibleInConsultorioState() {
        String expectedPatientDocument = apiConsumer.recall(ApiContextKeys.PATIENT_DOCUMENT);
        apiConsumer.should(
                seeThat(ResponseBodyQuestion.field("patientId"), notNullValue()),
                seeThat(ResponseBodyQuestion.field("patientId"), equalTo(expectedPatientDocument))
        );
    }

    @And("the consultorio should not have a linked doctor")
    public void theConsultorioShouldNotHaveALinkedDoctor() {
        apiConsumer.attemptsTo(
                WaitForConsultorioStateInteraction.toBe("SinMedico")
                        .withoutVisiblePatient()
                        .withoutLinkedDoctor()
        );
        apiConsumer.should(seeThat(ResponseBodyQuestion.field("medicoId"), nullValue()));
    }

    private void authenticateDoctorFromScratch() {
        String email = "api.medico." + System.currentTimeMillis() + "@testmail.local";
        String password = "Duver123--";

        apiConsumer.remember(ApiContextKeys.ACCOUNT_EMAIL, email);
        apiConsumer.remember(ApiContextKeys.ACCOUNT_PASSWORD, password);

        apiConsumer.attemptsTo(RegisterDoctorAccountTask.withCredentials(email, password));
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(201)));

        apiConsumer.attemptsTo(AuthenticateDoctorTask.withCredentials(email, password));
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(200)));

        waitForDoctorProvisioning();
    }

    private void prepareLinkedConsultorioFromScratch() {
        authenticateDoctorFromScratch();
        apiConsumer.attemptsTo(ResolveAvailableConsultorioInteraction.fromConfiguredRange());

        String consultorioId = apiConsumer.recall(ApiContextKeys.CONSULTORIO_ID);
        apiConsumer.attemptsTo(LinkDoctorToConsultorioTask.forConsultorio(consultorioId));
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(202)));
        apiConsumer.attemptsTo(
                WaitForConsultorioStateInteraction.toBe("ConMedicoDisponible")
                        .withoutVisiblePatient()
                        .withLinkedDoctor()
        );
    }

    private void createAndStartActiveAttention() {
        String patientName = "Paciente API " + System.currentTimeMillis();
        String patientDocument = String.valueOf(100000000 + (System.currentTimeMillis() % 899999999));

        apiConsumer.attemptsTo(CreatePatientInQueueTask.withData(patientName, patientDocument));
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(202)));

        apiConsumer.attemptsTo(StartMedicalAttentionTask.withRememberedPatient());
        apiConsumer.should(seeThat(ResponseStatusQuestion.theStatusCode(), equalTo(202)));

        apiConsumer.attemptsTo(
                WaitForConsultorioStateInteraction.toBe("EnAtencion")
                        .withVisiblePatient()
                        .withLinkedDoctor()
        );
    }

    private void waitForDoctorProvisioning() {
        try {
            Thread.sleep(3500);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Doctor provisioning wait was interrupted", exception);
        }
    }
}
