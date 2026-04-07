package com.autoapiscreenplay.screenplay.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartAttentionRequest {

    private String pacienteNombre;
    private String pacienteDocumento;
}
