package org.thony.junit5app.example.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thony.junit5app.example.models.Examen;
import org.thony.junit5app.example.repositories.ExamenRepository;
import org.thony.junit5app.example.repositories.ExamenRepositoryImpl;
import org.thony.junit5app.example.repositories.QuestionRepository;
import org.thony.junit5app.example.repositories.QuestionRepositoryImpl;
import org.thony.junit5app.example.utils.Datos;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // inyecta los mocks directo al servicio
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImpl examenRepository;
    @Mock
    QuestionRepositoryImpl questionRepository;

    @InjectMocks
    ExamenServiceImpl examenService;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
//        this.examenRepository = mock(ExamenRepository.class);
//        this.questionRepository = mock(QuestionRepository.class);
//        this.examenService = new ExamenServiceImpl(examenRepository, questionRepository);
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

    @Test
    void handlerExceptionTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS_NULL_IDS);
        when(questionRepository.findQuestionByExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> examenService.findExamenWithQuestionByIdExamen("Matematicas"));
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepository).findAll();
        verify(questionRepository).findQuestionByExamenId(isNull());
    }

    @Test
    void ArgumentMatchesTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);
        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenWithQuestionByIdExamen("Matematicas");

        verify(examenRepository).findAll();
        verify(questionRepository).findQuestionByExamenId(argThat(arg -> arg != null && arg >= 5L));
        verify(questionRepository).findQuestionByExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        verify(questionRepository).findQuestionByExamenId(eq(5L));
    }

    @Test
    void ArgumentMatches2Test() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS_NEGATIVES);
        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenWithQuestionByIdExamen("Matematicas");

        verify(examenRepository).findAll();
        verify(questionRepository).findQuestionByExamenId(argThat(new MiArgsMatchers()));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {
        private Long arg;

        @Override
        public boolean matches(Long arg) {
            this.arg = arg;
            return arg != null && arg > 0;
        }

        @Override
        public String toString() {
            return "MiArgsMatchers{" +
                    "arg=" + arg +
                    '}' + " " + "es para un mensaje personalizado de error, que imprime mockito en caso falle el test";
        }
    }

    @Test
    void argumentCaptureTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);
        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        examenService.findExamenWithQuestionByIdExamen("Matematicas");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(questionRepository).findQuestionByExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void doThrowTest() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        // utiliza esta forma de when cuando el method services es void
        doThrow(IllegalArgumentException.class).when(questionRepository).saveList(anyList());

        assertThrows(IllegalArgumentException.class, () -> examenService.save(examen));
    }

    @Test
    void saveExamenTest() {
        // Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        // La idea es poder añadir una secuencia en grabar un id
//        when(examenRepository.save(any(Examen.class))).then(new Answer<Examen>() {
//            Long secuencia = 1L;
//
//            @Override
//            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
//                Examen examen = invocationOnMock.getArgument(0);
//                examen.setId(secuencia++);
//                return examen;
//            }
//        });

        doAnswer(invocationOnMock -> {
            Long secuencia = 1L;

            Examen examen = invocationOnMock.getArgument(0);
            examen.setId(secuencia++);
            return examen;
        }).when(examenRepository).save(any(Examen.class));

        // when
        Examen examen = examenService.save(newExamen);

        // then
        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).save(any(Examen.class));
        verify(questionRepository).saveList(anyList());
    }

    @Test
    void doAnswerTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);

        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 5L ? Datos.PREGUNTAS : Collections.EMPTY_LIST;
        }).when(questionRepository).findQuestionByExamenId(anyLong());

        Examen examen = examenService.findExamenWithQuestionByIdExamen("Matematicas");
        assertEquals(4, examen.getPreguntas().size());
        assertEquals(5L, examen.getId());
        assertTrue(examen.getPreguntas().contains("Variables"));
        assertEquals("Matematicas", examen.getNombre());

        verify(questionRepository).findQuestionByExamenId(anyLong());
    }

    @Test
    void doCallRealMethodTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENS);
        // realiza un mock del method
//        when(questionRepository.findQuestionByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        // ingresa al metodo
        doCallRealMethod().when(questionRepository).findQuestionByExamenId(anyLong());

        Examen examen = examenService.findExamenWithQuestionByIdExamen("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }
}