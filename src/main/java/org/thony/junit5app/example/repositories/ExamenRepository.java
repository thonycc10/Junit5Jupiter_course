package org.thony.junit5app.example.repositories;

import org.thony.junit5app.example.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}
