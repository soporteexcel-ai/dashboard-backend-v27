package com.projecthq.productivity.dto.impacto;

/**
 * Representa la entrada bruta de un proyecto desde el frontend.
 * V-018: Uso de Java Record para inmutabilidad y legibilidad.
 */
public record ProjectInputDTO(
    String id,
    String name,
    String direction,
    String client,
    String manager,
    String analyst,
    String status,
    String stage,
    Double budget, // Si es nulo o 0, se trata como "Sin Impacto"
    String impactType,
    String projectComplexity
) {}
