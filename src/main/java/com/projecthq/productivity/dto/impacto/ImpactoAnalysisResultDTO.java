package com.projecthq.productivity.dto.impacto;

import java.util.List;

/**
 * Reporte consolidado de Impacto Monetario procesado en Backend.
 * V-020: Reporte jerárquico para el frontend React.
 */
public record ImpactoAnalysisResultDTO(
    FinancialKPIs kpis,
    List<DirectionSummaryItem> directions,
    List<LeaderSummaryItem> leadersNoImpact,
    List<LeaderImpactItem> leadersWithImpact
) {

    public record FinancialKPIs(
        long totalCount,
        long activeCount,
        long finalizedCount,
        long missingBudgetCount,
        double totalActiveBudget,
        double totalFinalizedBudget
    ) {}

    public record DirectionSummaryItem(
        String directionName,
        String shortName,
        long analizados,
        long conImpacto,
        long sinImpactoActivo,
        long sinImpactoFinalizado,
        double valorActivo,
        double valorFinalizado,
        List<ClientSummaryItem> clients
    ) {}

    public record ClientSummaryItem(
        String name,
        long analizados,
        long conImpacto,
        long sinImpactoActivo,
        long sinImpactoFinalizado,
        double valorActivo,
        double valorFinalizado
    ) {}

    public record LeaderSummaryItem(
        String directionName,
        String dirShort,
        String leaderName,
        long active,
        long finalized
    ) {}

    public record LeaderImpactItem(
        String directionName,
        String dirShort,
        String leaderName,
        double totalBudget,
        double activeBudget,
        double finalizedBudget,
        long count
    ) {}
}
