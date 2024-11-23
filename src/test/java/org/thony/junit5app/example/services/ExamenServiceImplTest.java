package org.thony.junit5app.example.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thony.junit5app.example.models.Examen;
import org.thony.junit5app.example.repositories.ExamenRepository;
import org.thony.junit5app.example.repositories.QuestionRepository;
import org.thony.junit5app.example.util.Datos;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class ExamenServiceImplTest {
    ExamenRepository examenRepository;
    QuestionRepository questionRepository;
    ExamenService examenService;

    @BeforeEach
    void setUp() {
        this.examenRepository = mock(ExamenRepository.class);
        this.questionRepository = mock(QuestionRepository.class);
        this.examenService = new ExamenServiceImpl(examenRepository, questionRepository);
    }

    @Test
    void findExamenByNameTest() {
        List<Examen> examenList = List.of(new Examen(1L, "Thony"),
                new Examen(2L, "Laura"),
                new Examen(3L, "Camile"));

        when(examenRepository.findAll()).thenReturn(examenList);

        Optional<Examen> examenOptional = examenService.findExamenByName("Thony");

        assertTrue(examenOptional.isPresent());
        assertEquals(1L, examenOptional.get().getId());
        assertEquals("Thony", examenOptional.orElseThrow().getNombre());
    }

    @Test
    void listExamenIsEmptyTest() {
        List<Examen> examenList = Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(examenList);

        Optional<Examen> examen = examenService.findExamenByName("Thony");

        assertFalse(examen.isPresent());
    }

    @Test
    void findExamenWithQuestionByIdExamenTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);
        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = examenService.findExamenWithQuestionByIdExamen("Matematicas");
        assertEquals(4, examen.getPreguntas().size());
    }

    @Test
    void findExamenWithQuestionByIdExamenVerifyTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);
        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = examenService.findExamenWithQuestionByIdExamen("Matematicas");
        assertEquals(4, examen.getPreguntas().size());

//        verify ayuda a verificar que se ejecute el metodo si se quita en el servicio real fallaria.
        verify(examenRepository).findAll();
        verify(questionRepository).findQuestionByExamenId(anyLong());
    }

    @Test
    void notfindExamenWithQuestionByIdExamenVerifyTest() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = examenService.findExamenWithQuestionByIdExamen("Matematicas2");
        assertNull(examen);
//        verify ayuda a verificar que se ejecute el metodo si se quita en el servicio real fallaria.
        verify(examenRepository).findAll();
        verify(questionRepository).findQuestionByExamenId(anyLong());
    }
}