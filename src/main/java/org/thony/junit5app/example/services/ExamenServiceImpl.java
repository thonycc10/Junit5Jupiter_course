package org.thony.junit5app.example.services;

import org.thony.junit5app.example.models.Examen;
import org.thony.junit5app.example.repositories.ExamenRepository;
import org.thony.junit5app.example.repositories.QuestionRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {
    private ExamenRepository examenRepository;
    private QuestionRepository questionRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, QuestionRepository questionRepository) {
        this.examenRepository = examenRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Examen> findExamenByName(String name) {
        return examenRepository.findAll()
                .stream()
                .filter(examn -> examn.getNombre().contains(name))
                .findFirst();
    }

    @Override
    public Examen findExamenWithQuestionByIdExamen(String name) {
        Optional<Examen> examenOptional = findExamenByName(name);
        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            examen.setPreguntas(questionRepository.findQuestionByExamenId(examen.getId()));
        }
        return examen;
    }
}
