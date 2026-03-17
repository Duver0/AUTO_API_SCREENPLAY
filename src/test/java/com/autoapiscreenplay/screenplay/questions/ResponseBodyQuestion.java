package com.autoapiscreenplay.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;

public class ResponseBodyQuestion implements Question<String> {

    private final String fieldPath;

    public ResponseBodyQuestion(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public static ResponseBodyQuestion field(String fieldPath) {
        return new ResponseBodyQuestion(fieldPath);
    }

    @Override
    public String answeredBy(Actor actor) {
        Object value = LastResponse.received().answeredBy(actor).jsonPath().get(fieldPath);
        return value == null ? null : String.valueOf(value);
    }
}
