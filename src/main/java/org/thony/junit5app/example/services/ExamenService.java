package org.thony.junit5app.example.services;

import org.thony.junit5app.example.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenByName(String name);
    Examen findExamenWithQuestionByIdExamen(String name);
    Examen save(Examen examen);
}
