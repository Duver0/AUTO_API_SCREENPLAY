# AUTO_API_SCREENPLAY

Automatizacion de API REST con Screenplay, Serenity BDD y Serenity Rest. El proyecto valida flujos de autenticacion, turnos y ciclo de vida de consultorio contra un backend local.

## 1. Descripcion

Las pruebas consumen `http://localhost:3000/` y modelan cada operacion HTTP como una Task independiente, manteniendo pasos declarativos en Gherkin.

Stack principal:
- Java 17+
- Serenity BDD + Serenity Rest
- Cucumber con JUnit4
- Gradle Wrapper

## 2. Prerrequisitos y configuracion

- Java 17 o superior
- API objetivo disponible en `http://localhost:3000/`
- Docker Compose de `IA_P1` levantado (producer en puerto 3000)

Base URL en `serenity.conf`:

```hocon
environments {
  default {
    base.url = "http://localhost:3000/"
  }
}
```

## 3. Ejecucion

Suite completa:

```bash
./gradlew clean test
```

Runner especifico ciclo de vida de consultorio:

```bash
./gradlew clean test --tests com.autoapiscreenplay.runner.CicloVidaConsultorioRunner
```

Generar reporte Serenity:

```bash
./gradlew aggregate
```

To open it from Linux after generation:

```bash
xdg-open target/site/serenity/index.html
```

Reporte HTML: `target/site/serenity/index.html`

## 4. Features activas

### api_flow.feature

Escenario `@CRUD` para validar flujo base:
- `POST /auth/signUp`
- `POST /auth/signIn`
- `POST /turnos`
- `GET /turnos`

### ciclo_vida_consultorio.feature

Escenarios independientes bajo tag `@ciclo_vida_consultorio`:
- `POST /auth/signUp` y `POST /auth/signIn`
- `POST /medicos/consultorio/asignar`
- `POST /medicos/atencion/iniciar`
- `POST /medicos/atencion/finalizar`
- `PATCH /medicos/disponibilidad`
- `POST /medicos/consultorio/liberar`
- `GET /medicos/consultorio/estado/{consultorioId}`

## 5. Estructura del proyecto

```text
src/
  test/
    java/
      com.autoapiscreenplay/
        runner/
          CucumberTestRunner.java
          CicloVidaConsultorioRunner.java
        screenplay/
          actors/
            ApiActorFactory.java
          context/
            ApiContextKeys.java
          interactions/
            ResolveAvailableConsultorioInteraction.java
            WaitForConsultorioStateInteraction.java
          models/
            AppointmentRequest.java
            AssignConsultorioRequest.java
            SetAvailabilityRequest.java
            SignInRequest.java
            SignUpRequest.java
            StartAttentionRequest.java
          questions/
            ResponseBodyQuestion.java
            ResponseStatusQuestion.java
          tasks/
            AuthenticateDoctorTask.java
            CreateAccountTask.java
            CreateAppointmentTask.java
            CreatePatientInQueueTask.java
            FinishMedicalAttentionTask.java
            GetAppointmentQueueTask.java
            GetConsultorioStateTask.java
            LinkDoctorToConsultorioTask.java
            RegisterDoctorAccountTask.java
            ReleaseConsultorioTask.java
            SignInTask.java
            StartMedicalAttentionTask.java
            UpdateAvailabilityTask.java
        steps/
          ConsultorioLifecycleSteps.java
          MedicalWorkflowSteps.java
    resources/
      features/
        api_flow.feature
        ciclo_vida_consultorio.feature
```
