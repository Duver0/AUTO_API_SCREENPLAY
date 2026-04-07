package com.autoapiscreenplay.runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features/ciclo_vida_consultorio.feature",
        glue = "com.autoapiscreenplay.steps",
        tags = "@ciclo_vida_consultorio"
)
public class CicloVidaConsultorioRunner {
}
