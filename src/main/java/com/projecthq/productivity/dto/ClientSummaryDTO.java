package com.projecthq.productivity.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientSummaryDTO {
    private String clientName;
    private int analizados;
    private int conImpacto;
    private int sinImpacto;
    private double valorActivo;
    private double valorFinalizado;
}
