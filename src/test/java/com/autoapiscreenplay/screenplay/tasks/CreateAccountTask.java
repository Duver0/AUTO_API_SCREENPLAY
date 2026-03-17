package com.autoapiscreenplay.screenplay.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import com.autoapiscreenplay.screenplay.models.SignUpRequest;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateAccountTask implements Task {

    private final String email;
    private final String password;

    public CreateAccountTask(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static CreateAccountTask withData(String email, String password) {
        return instrumented(CreateAccountTask.class, email, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        SignUpRequest request = SignUpRequest.builder()
                .email(email)
                .password(password)
                .nombre("QA Medical User")
                .rol("admin")
                .build();

        actor.attemptsTo(
            Post.to("auth/signUp")
                        .with(specification -> specification
                                .contentType(ContentType.JSON)
                                .body(request))
        );
    }
}
