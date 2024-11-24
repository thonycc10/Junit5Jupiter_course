package org.thony.junit5app.example.repositories;

import org.thony.junit5app.example.models.Examen;
import org.thony.junit5app.example.utils.Datos;

import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository {
    @Override
    public List<Examen> findAll() {
        return Datos.EXAMENS;
    }

    @Override
    public Examen save(Examen examen) {
        return Datos.EXAMEN;
    }
}
