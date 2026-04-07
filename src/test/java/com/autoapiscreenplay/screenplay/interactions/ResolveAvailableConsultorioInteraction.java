package com.autoapiscreenplay.screenplay.interactions;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.questions.ResponseBodyQuestion;
import com.autoapiscreenplay.screenplay.questions.ResponseStatusQuestion;
import com.autoapiscreenplay.screenplay.tasks.GetConsultorioStateTask;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;

public class ResolveAvailableConsultorioInteraction implements Interaction {

    private final int consultoriosTotal;

    public ResolveAvailableConsultorioInteraction(int consultoriosTotal) {
        this.consultoriosTotal = consultoriosTotal;
    }

    public static ResolveAvailableConsultorioInteraction fromConfiguredRange() {
        return Tasks.instrumented(ResolveAvailableConsultorioInteraction.class, resolveConsultoriosTotal());
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        for (int index = 1; index <= consultoriosTotal; index++) {
            String consultorioId = "C" + index;
            actor.attemptsTo(GetConsultorioStateTask.forConsultorio(consultorioId));

            Integer status = ResponseStatusQuestion.theStatusCode().answeredBy(actor);
            String state = ResponseBodyQuestion.field("estado").answeredBy(actor);

            if (status == 200 && "SinMedico".equals(state)) {
                actor.remember(ApiContextKeys.CONSULTORIO_ID, consultorioId);
                return;
            }
        }

        throw new IllegalStateException("No available consultorio was found in state SinMedico");
    }

    private static int resolveConsultoriosTotal() {
        String raw = System.getenv("NEXT_PUBLIC_CONSULTORIOS_TOTAL");
        if (raw == null || raw.isBlank()) {
            return 5;
        }

        try {
            int parsed = Integer.parseInt(raw);
            return parsed > 0 ? parsed : 5;
        } catch (NumberFormatException exception) {
            return 5;
        }
    }
}
