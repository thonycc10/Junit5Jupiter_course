package org.thony.junit5app.example.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thony.junit5app.example.models.Examen;
import org.thony.junit5app.example.repositories.ExamenRepositoryImpl;
import org.thony.junit5app.example.repositories.QuestionRepositoryImpl;
import org.thony.junit5app.example.utils.Datos;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // inyecta los mocks directo al servicio
class ExamenServiceImplSpyTest {

    @Spy
    ExamenRepositoryImpl examenRepository;
    @Spy
    QuestionRepositoryImpl questionRepository;
    @InjectMocks
    ExamenServiceImpl examenService;

    @Test
    void spyTest() {
        List<String> questions = List.of("Aritmetica");

//        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(questions); // ingresa al metodo por @SPY
        doReturn(questions).when(questionRepository).findQuestionByExamenId(anyLong()); // mock

        Examen examne = examenService.findExamenWithQuestionByIdExamen("Matematicas");
        assertEquals(5, examne.getId());
        assertEquals("Matematicas", examne.getNombre());
        assertEquals(1, examne.getPreguntas().size());
        assertTrue(examne.getPreguntas().contains("Aritmetica"));

        verify(examenRepository).findAll();
        verify(questionRepository).findQuestionByExamenId(anyLong());
    }

    @Test
    void orderMock() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);

        examenService.findExamenWithQuestionByIdExamen("Matematicas");
        examenService.findExamenWithQuestionByIdExamen("Algebra");

        InOrder inOrder = inOrder(examenRepository, questionRepository);
        inOrder.verify(questionRepository).findQuestionByExamenId(5L);
        inOrder.verify(questionRepository).findQuestionByExamenId(7L);
    }

    @Test
    void orderMock2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);

        examenService.findExamenWithQuestionByIdExamen("Matematicas");
        examenService.findExamenWithQuestionByIdExamen("Algebra");

        InOrder inOrder = inOrder(examenRepository, questionRepository);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(questionRepository).findQuestionByExamenId(5L);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(questionRepository).findQuestionByExamenId(7L);
    }
}