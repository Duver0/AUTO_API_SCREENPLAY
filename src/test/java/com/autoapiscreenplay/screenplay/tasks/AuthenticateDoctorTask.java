package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.models.SignInRequest;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class AuthenticateDoctorTask implements Task {

    private final String email;
    private final String password;

    public AuthenticateDoctorTask(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static AuthenticateDoctorTask withCredentials(String email, String password) {
        return instrumented(AuthenticateDoctorTask.class, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        SignInRequest request = SignInRequest.builder()
                .email(email)
                .password(password)
                .build();

        actor.attemptsTo(
                Post.to("auth/signIn")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .body(request))
        );

        String token = LastResponse.received().answeredBy(actor).jsonPath().getString("token");
        String doctorId = LastResponse.received().answeredBy(actor).jsonPath().getString("usuario.id");

        actor.remember(ApiContextKeys.AUTH_TOKEN, token);
        actor.remember(ApiContextKeys.DOCTOR_ID, doctorId);
    }
}
