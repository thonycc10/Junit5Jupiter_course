package org.thony.junit5app.example.util;

import org.thony.junit5app.example.models.Examen;

import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENS = List.of(new Examen(5L, "Matematicas"),
                                                       new Examen(6L, "Razonamiento Matematico"),
                                                       new Examen(7L, "Algebra"));

    public final static  List<String> PREGUNTAS = List.of("Logica", "Algoritmos", "Variables", "Operaciones");
}
