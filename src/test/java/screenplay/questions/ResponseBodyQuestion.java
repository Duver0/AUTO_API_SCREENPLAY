package screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseBodyQuestion implements Question<String> {

    public static ResponseBodyQuestion theBody() {
        return new ResponseBodyQuestion();
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().getBody().asString();
    }
}
