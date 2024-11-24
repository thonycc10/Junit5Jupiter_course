package org.thony.junit5app.example.utils;

import org.thony.junit5app.example.models.Examen;

import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENS = List.of(new Examen(5L, "Matematicas"),
                                                       new Examen(6L, "Razonamiento Matematico"),
                                                       new Examen(7L, "Algebra"));

    public final static List<Examen> EXAMENS_NEGATIVES = List.of(new Examen(-5L, "Matematicas"),
                                                       new Examen(-6L, "Razonamiento Matematico"),
                                                       new Examen(null, "Algebra"));

    public final static List<Examen> EXAMENS_NULL_IDS = List.of(new Examen(null, "Matematicas"),
                                                       new Examen(null, "Razonamiento Matematico"),
                                                       new Examen(null, "Algebra"));

    public final static  List<String> PREGUNTAS = List.of("Logica", "Algoritmos", "Variables", "Operaciones");
    public final static  Examen EXAMEN = new Examen(null, "Fisica");
}
