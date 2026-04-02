package com.projecthq.productivity.factory;

import com.projecthq.productivity.model.BAProductivityRecord;
import java.time.LocalDate;
import java.util.List;

/**
 * Fábrica de datos sintéticos (Mock Data).
 * REGLA DE PROCESAMIENTO: Máximo 5 registros.
 */
public class MockDataFactory {
    
    public static List<BAProductivityRecord> generateMockData() {
        return List.of(
            new BAProductivityRecord("Carlos Perez", 5, 3, 2, 0.25, LocalDate.now()),
            new BAProductivityRecord("Maria Gomez", 3, 2, 4, 0.10, LocalDate.now()),
            new BAProductivityRecord("Juan Rodriguez", 7, 5, 1, 0.50, LocalDate.now()),
            new BAProductivityRecord("Ana Martinez", 2, 4, 3, 0.05, LocalDate.now()),
            new BAProductivityRecord("Luis Fernandez", 4, 1, 5, 0.15, LocalDate.now())
        );
    }
}
