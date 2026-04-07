package com.autoapiscreenplay.screenplay.tasks;

import com.autoapiscreenplay.screenplay.models.SignUpRequest;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class RegisterDoctorAccountTask implements Task {

    private final String email;
    private final String password;

    public RegisterDoctorAccountTask(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static RegisterDoctorAccountTask withCredentials(String email, String password) {
        return instrumented(RegisterDoctorAccountTask.class, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        SignUpRequest request = SignUpRequest.builder()
                .email(email)
                .password(password)
                .nombre("API Doctor")
                .rol("medico")
                .build();

        actor.attemptsTo(
                Post.to("auth/signUp")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .body(request))
        );
    }
}
