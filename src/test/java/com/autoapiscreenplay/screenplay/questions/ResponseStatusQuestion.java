package com.autoapiscreenplay.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;

public class ResponseStatusQuestion implements Question<Integer> {

    public static ResponseStatusQuestion theStatusCode() {
        return new ResponseStatusQuestion();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return LastResponse.received().answeredBy(actor).statusCode();
    }
}
