package org.thony.junit5app.example.repositories;

import java.util.List;

public interface QuestionRepository {
    List<String> findQuestionByExamenId(Long id);
}
