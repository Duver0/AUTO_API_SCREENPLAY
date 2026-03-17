package com.autoapiscreenplay.screenplay.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import com.autoapiscreenplay.screenplay.models.SignInRequest;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SignInTask implements Task {

    private final String email;
    private final String password;

    public SignInTask(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignInTask withCredentials(String email, String password) {
        return instrumented(SignInTask.class, email, password);
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
    }
}
