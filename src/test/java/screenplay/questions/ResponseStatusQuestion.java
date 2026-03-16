package screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseStatusQuestion implements Question<Integer> {

    public static ResponseStatusQuestion theStatusCode() {
        return new ResponseStatusQuestion();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().getStatusCode();
    }
}
