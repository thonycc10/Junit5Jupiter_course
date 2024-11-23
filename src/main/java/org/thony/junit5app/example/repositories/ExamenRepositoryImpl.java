package org.thony.junit5app.example.repositories;

import org.thony.junit5app.example.models.Examen;

import java.util.ArrayList;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository {
    @Override
    public List<Examen> findAll() {
        return List.of(new Examen(1L, "Thony"),
                new Examen(2L, "Laura"),
                new Examen(3L, "Camile"));
    }
}
