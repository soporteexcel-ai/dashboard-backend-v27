package com.projecthq.productivity.service;

import com.projecthq.productivity.factory.MockDataFactory;
import com.projecthq.productivity.model.BAProductivityRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio modular para el procesamiento de productividad.
 * V-015: Programación funcional con Streams API y Optional.
 * V-016: Inyección por constructor (Lombok @RequiredArgsConstructor).
 */
@Service
@RequiredArgsConstructor
public class ProductivityService {

    /**
     * Procesa la productividad filtrando por un rango de fechas opcional.
     * REGLA DE ARQUITECTURA: Uso de Streams para filtrado eficiente.
     */
    public List<BAProductivityRecord> getMonthlyProductivity(LocalDate startDate, LocalDate endDate, String baName) {
        List<BAProductivityRecord> allData = MockDataFactory.generateMockData();

        return allData.stream()
            .filter(record -> isWithinRange(record.reportDate(), startDate, endDate))
            .filter(record -> baName == null || record.baName().equalsIgnoreCase(baName))
            .toList(); // Java 16+ simple toList()
    }

    private boolean isWithinRange(LocalDate date, LocalDate start, LocalDate end) {
        return Optional.ofNullable(date)
            .map(d -> (start == null || !d.isBefore(start)) && (end == null || !d.isAfter(end)))
            .orElse(false);
    }
}
