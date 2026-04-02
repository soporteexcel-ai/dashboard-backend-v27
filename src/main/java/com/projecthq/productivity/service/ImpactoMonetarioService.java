package com.projecthq.productivity.service;

import com.projecthq.productivity.dto.impacto.ImpactoAnalysisResultDTO;
import com.projecthq.productivity.dto.impacto.ImpactoAnalysisResultDTO.*;
import com.projecthq.productivity.dto.impacto.ProjectInputDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para el procesamiento analítico del impacto monetario.
 * Java 17+ Modern & Lean.
 */
@Service
public class ImpactoMonetarioService {

    public ImpactoAnalysisResultDTO calculateImpact(List<ProjectInputDTO> projects) {
        if (projects == null) return null;

        // 1. Filtrar los proyectos en Scope (Criterio del Usuario: 135 Analizados)
        List<ProjectInputDTO> inScope = projects.stream()
            .filter(p -> {
                String status = Optional.ofNullable(p.status()).orElse("").toLowerCase();
                String stage = Optional.ofNullable(p.stage()).orElse("").toLowerCase();
                
                boolean isFinalized = status.contains("finalizado");
                
                // Desarrollo, Pruebas UAT, Despliegue, Producción o Proyecto
                boolean isKeyStage = stage.contains("desarrollo") || 
                                     stage.contains("uat") || 
                                     stage.contains("despliegue") || 
                                     stage.contains("produccion") || 
                                     stage.contains("producción") ||
                                     stage.contains("proyecto") ||
                                     stage.contains("estabiliza");

                return isFinalized || isKeyStage;
            })
            .collect(Collectors.toList());

        System.err.println("[DIAGNOSTICO] Total Recibidos: " + projects.size());

        // 2. Calcular KPIs Generales
        FinancialKPIs kpis = calculateKPIs(inScope);

        // 3. Resumen por Dirección y Cliente
        List<DirectionSummaryItem> directions = calculateDirectionSummary(inScope);

        // 4. Resumen por Líderes (Sin Impacto)
        List<LeaderSummaryItem> leadersNoImpact = calculateLeaderSummaryNoImpact(inScope);

        // 5. Resumen por Líderes (Con Impacto)
        List<LeaderImpactItem> leadersWithImpact = calculateLeaderImpactSummary(inScope);

        return new ImpactoAnalysisResultDTO(kpis, directions, leadersNoImpact, leadersWithImpact);
    }

    private boolean isFinished(ProjectInputDTO p) {
        String status = Optional.ofNullable(p.status()).orElse("").toLowerCase();
        return status.contains("finalizado");
    }

    private FinancialKPIs calculateKPIs(List<ProjectInputDTO> projects) {
        long totalCount = projects.size();
        long finalizedCount = projects.stream().filter(this::isFinished).count();
        long activeCount = totalCount - finalizedCount;
        long missingBudgetCount = projects.stream().filter(p -> p.budget() == null || p.budget() == 0).count();
        
        double totalFinalizedBudget = projects.stream()
            .filter(p -> isFinished(p) && p.budget() != null)
            .mapToDouble(ProjectInputDTO::budget)
            .sum();

        double totalActiveBudget = projects.stream()
            .filter(p -> !isFinished(p) && p.budget() != null)
            .mapToDouble(ProjectInputDTO::budget)
            .sum();

        return new FinancialKPIs(totalCount, activeCount, finalizedCount, missingBudgetCount, totalActiveBudget, totalFinalizedBudget);
    }

    private List<DirectionSummaryItem> calculateDirectionSummary(List<ProjectInputDTO> projects) {
        Map<String, List<ProjectInputDTO>> groupedByDir = projects.stream()
            .collect(Collectors.groupingBy(p -> Optional.ofNullable(p.direction()).orElse("Otras")));

        return groupedByDir.entrySet().stream()
            .map(entry -> {
                String dirName = entry.getKey();
                List<ProjectInputDTO> dirProjects = entry.getValue();

                Map<String, List<ProjectInputDTO>> groupedByClient = dirProjects.stream()
                    .collect(Collectors.groupingBy(p -> Optional.ofNullable(p.client()).orElse("Sin Cliente")));

                List<ClientSummaryItem> clients = groupedByClient.entrySet().stream()
                    .map(cEntry -> createClientSummary(cEntry.getKey(), cEntry.getValue()))
                    .sorted(Comparator.comparingLong(ClientSummaryItem::analizados).reversed())
                    .toList();

                return new DirectionSummaryItem(
                    dirName,
                    formatShortName(dirName),
                    dirProjects.size(),
                    dirProjects.stream().filter(p -> p.budget() != null && p.budget() > 0).count(),
                    dirProjects.stream().filter(p -> (p.budget() == null || p.budget() == 0) && !isFinished(p)).count(),
                    dirProjects.stream().filter(p -> (p.budget() == null || p.budget() == 0) && isFinished(p)).count(),
                    dirProjects.stream().filter(p -> !isFinished(p) && p.budget() != null).mapToDouble(ProjectInputDTO::budget).sum(),
                    dirProjects.stream().filter(p -> isFinished(p) && p.budget() != null).mapToDouble(ProjectInputDTO::budget).sum(),
                    clients
                );
            })
            .sorted(Comparator.comparing(DirectionSummaryItem::shortName))
            .toList();
    }

    private ClientSummaryItem createClientSummary(String name, List<ProjectInputDTO> clientProjs) {
        return new ClientSummaryItem(
            name,
            clientProjs.size(),
            clientProjs.stream().filter(p -> p.budget() != null && p.budget() > 0).count(),
            clientProjs.stream().filter(p -> (p.budget() == null || p.budget() == 0) && !isFinished(p)).count(),
            clientProjs.stream().filter(p -> (p.budget() == null || p.budget() == 0) && isFinished(p)).count(),
            clientProjs.stream().filter(p -> !isFinished(p) && p.budget() != null).mapToDouble(ProjectInputDTO::budget).sum(),
            clientProjs.stream().filter(p -> isFinished(p) && p.budget() != null).mapToDouble(ProjectInputDTO::budget).sum()
        );
    }

    private List<LeaderSummaryItem> calculateLeaderSummaryNoImpact(List<ProjectInputDTO> projects) {
        return projects.stream()
            .filter(p -> p.budget() == null || p.budget() == 0)
            .collect(Collectors.groupingBy(p -> p.direction() + "||" + (p.manager() == null ? "Sin Líder" : p.manager())))
            .values().stream()
            .map(leaderProjs -> {
                ProjectInputDTO first = leaderProjs.get(0);
                String leader = first.manager() == null ? "Sin Líder" : first.manager();
                return new LeaderSummaryItem(
                    first.direction(),
                    formatShortName(first.direction()),
                    leader,
                    leaderProjs.stream().filter(lp -> !isFinished(lp)).count(),
                    leaderProjs.stream().filter(lp -> isFinished(lp)).count()
                );
            })
            .sorted(Comparator.comparing(LeaderSummaryItem::directionName).thenComparing(LeaderSummaryItem::leaderName))
            .toList();
    }

    private List<LeaderImpactItem> calculateLeaderImpactSummary(List<ProjectInputDTO> projects) {
        return projects.stream()
            .filter(p -> p.budget() != null && p.budget() > 0)
            .collect(Collectors.groupingBy(p -> p.direction() + "||" + (p.manager() == null ? "Sin Líder" : p.manager())))
            .values().stream()
            .map(leaderProjs -> {
                ProjectInputDTO first = leaderProjs.get(0);
                String leader = first.manager() == null ? "Sin Líder" : first.manager();
                double total = leaderProjs.stream().mapToDouble(ProjectInputDTO::budget).sum();
                double active = leaderProjs.stream().filter(lp -> !isFinished(lp)).mapToDouble(ProjectInputDTO::budget).sum();
                double finalized = total - active;
                
                return new LeaderImpactItem(
                    first.direction(),
                    formatShortName(first.direction()),
                    leader,
                    total,
                    active,
                    finalized,
                    leaderProjs.size()
                );
            })
            .sorted(Comparator.comparingDouble(LeaderImpactItem::totalBudget).reversed())
            .toList();
    }

    private String formatShortName(String full) {
        if (full == null) return "Otras";
        String s = full.toUpperCase()
            .replace("DIRECCIÓN DE ", "")
            .replace("DIRECCIÓN ", "")
            .replace("DIRECCIÖN ", "")
            .replace("DIRECCIÃ“N ", "")
            .replace("DIRECCION ", "")
            .trim();

        if (s.contains("BOGOTA") || s.contains("BOGOTÁ")) return "Bogotá";
        if (s.contains("MEDELLIN") || s.contains("MEDELLÍN")) return "Medellín";
        if (s.contains("ESTRATEG")) return "Proyectos Estratégicos";
        if (s.contains("TIGO")) return "Tigo";
        if (s.contains("VENTAS")) return "Ventas";
        if (s.contains("EMTELCO")) return "Estrategicos y Administrativos";
        
        // Para cualquier otro, Capitalize
        if (s.length() <= 1) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
