package com.projecthq.productivity.controller;

import com.projecthq.productivity.dto.impacto.ImpactoAnalysisResultDTO;
import com.projecthq.productivity.dto.impacto.ProjectInputDTO;
import com.projecthq.productivity.service.ImpactoMonetarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para la gestión del impacto monetario.
 * V-021: Uso de inyección por constructor.
 */
@RestController
@RequestMapping("/api/v1/impacto")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImpactoMonetarioController {

    private final ImpactoMonetarioService impactoMonetarioService;

    /**
     * Procesa la lista de proyectos para generar el análisis consolidado.
     * @param projects Lista bruta de proyectos desde el frontend.
     * @return Análisis detallado (KPIs, Direcciones, Líderes).
     */
    @PostMapping("/analizar")
    public ResponseEntity<ImpactoAnalysisResultDTO> analizarImpacto(@RequestBody List<ProjectInputDTO> projects) {
        ImpactoAnalysisResultDTO result = impactoMonetarioService.calculateImpact(projects);
        return ResponseEntity.ok(result);
    }
}
