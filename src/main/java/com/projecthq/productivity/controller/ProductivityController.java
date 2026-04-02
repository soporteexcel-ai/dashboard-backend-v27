package com.projecthq.productivity.controller;

import com.projecthq.productivity.model.BAProductivityRecord;
import com.projecthq.productivity.service.ProductivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST interactivo para el frontend React.
 * V-017: Uso de constructor injection (RequiredArgsConstructor).
 */
@RestController
@RequestMapping("/api/v1/productivity")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para desarrollo local con React
public class ProductivityController {

    private final ProductivityService productivityService;

    /**
     * Endpoint para obtener la productividad mensual con filtros opcionales.
     */
    @GetMapping("/ba")
    public ResponseEntity<List<BAProductivityRecord>> getBAProductivity(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String baName) {
        
        List<BAProductivityRecord> result = productivityService.getMonthlyProductivity(startDate, endDate, baName);
        return ResponseEntity.ok(result);
    }
}
