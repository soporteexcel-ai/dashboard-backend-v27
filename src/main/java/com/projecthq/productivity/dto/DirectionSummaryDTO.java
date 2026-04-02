package com.projecthq.productivity.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectionSummaryDTO {
    private String directionName;
    private String shortName;
    private int analizados;
    private int conImpacto;
    private int sinImpacto;
    private double valorActivo;
    private double valorFinalizado;
    private List<ClientSummaryDTO> clients; // NIVEL 2.5
}
