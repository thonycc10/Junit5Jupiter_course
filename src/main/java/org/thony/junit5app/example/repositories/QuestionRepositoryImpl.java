package org.thony.junit5app.example.repositories;

import org.thony.junit5app.example.utils.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestionRepositoryImpl implements QuestionRepository {
    @Override
    public List<String> findQuestionByExamenId(Long id) {
        System.out.println("QuestionRepositoryImpl.findQuestionByExamenId");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Datos.PREGUNTAS;
    }

    @Override
    public void saveList(List<String> questions) {
        System.out.println("QuestionRepositoryImpl.saveList");
    }
}
