package com.projecthq.productivity.model;

import java.time.LocalDate;

/**
 * Record que representa la productividad de un Business Analyst (BA).
 * V-012: Uso de Java Records para eliminar boilerplate.
 */
public record BAProductivityRecord(
    String baName,
    int wipProjects,
    int proposals,
    int completedProjects,
    double stopperRatio,
    LocalDate reportDate
) {}
