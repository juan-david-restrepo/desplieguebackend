package com.reporteloya.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidarTokenResponse {
    private boolean valido;
    private String motivo;
    private Long segundosRestantes;
}
