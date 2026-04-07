package com.autoapiscreenplay.screenplay.interactions;

import com.autoapiscreenplay.screenplay.context.ApiContextKeys;
import com.autoapiscreenplay.screenplay.questions.ResponseBodyQuestion;
import com.autoapiscreenplay.screenplay.questions.ResponseStatusQuestion;
import com.autoapiscreenplay.screenplay.tasks.GetConsultorioStateTask;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;

public class WaitForConsultorioStateInteraction implements Interaction {

    private final String expectedState;
    private final Boolean expectVisiblePatient;
    private final Boolean expectLinkedDoctor;

    public WaitForConsultorioStateInteraction(String expectedState, Boolean expectVisiblePatient, Boolean expectLinkedDoctor) {
        this.expectedState = expectedState;
        this.expectVisiblePatient = expectVisiblePatient;
        this.expectLinkedDoctor = expectLinkedDoctor;
    }

    public static WaitForConsultorioStateInteraction toBe(String expectedState) {
        return Tasks.instrumented(WaitForConsultorioStateInteraction.class, expectedState, null, null);
    }

    public WaitForConsultorioStateInteraction withVisiblePatient() {
        return Tasks.instrumented(WaitForConsultorioStateInteraction.class, expectedState, true, expectLinkedDoctor);
    }

    public WaitForConsultorioStateInteraction withoutVisiblePatient() {
        return Tasks.instrumented(WaitForConsultorioStateInteraction.class, expectedState, false, expectLinkedDoctor);
    }

    public WaitForConsultorioStateInteraction withLinkedDoctor() {
        return Tasks.instrumented(WaitForConsultorioStateInteraction.class, expectedState, expectVisiblePatient, true);
    }

    public WaitForConsultorioStateInteraction withoutLinkedDoctor() {
        return Tasks.instrumented(WaitForConsultorioStateInteraction.class, expectedState, expectVisiblePatient, false);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String consultorioId = actor.recall(ApiContextKeys.CONSULTORIO_ID);
        String expectedDoctorId = actor.recall(ApiContextKeys.DOCTOR_ID);

        for (int attempt = 1; attempt <= 30; attempt++) {
            actor.attemptsTo(GetConsultorioStateTask.forConsultorio(consultorioId));

            Integer status = ResponseStatusQuestion.theStatusCode().answeredBy(actor);
            String state = ResponseBodyQuestion.field("estado").answeredBy(actor);
            String patientId = ResponseBodyQuestion.field("patientId").answeredBy(actor);
            String doctorId = ResponseBodyQuestion.field("medicoId").answeredBy(actor);

            if (status != 200) {
                pause();
                continue;
            }

            if (!expectedState.equals(state)) {
                pause();
                continue;
            }

            if (expectVisiblePatient != null) {
                boolean hasPatient = patientId != null && !patientId.isBlank() && !"null".equalsIgnoreCase(patientId);
                if (expectVisiblePatient && !hasPatient) {
                    pause();
                    continue;
                }
                if (!expectVisiblePatient && hasPatient) {
                    pause();
                    continue;
                }
            }

            if (expectLinkedDoctor != null) {
                boolean hasDoctor = doctorId != null && !doctorId.isBlank() && !"null".equalsIgnoreCase(doctorId);
                if (expectLinkedDoctor && !hasDoctor) {
                    pause();
                    continue;
                }
                if (expectLinkedDoctor && hasDoctor && expectedDoctorId != null && !expectedDoctorId.equals(doctorId)) {
                    pause();
                    continue;
                }
                if (!expectLinkedDoctor && hasDoctor) {
                    pause();
                    continue;
                }
            }

            return;
        }

        throw new IllegalStateException("Consultorio state did not reach expected state: " + expectedState);
    }

    private void pause() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Polling execution interrupted", exception);
        }
    }
}
