package online.ahassan.quizmonoapp.repositories;

import online.ahassan.quizmonoapp.entities.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Page<Question> findAllByCategoryIgnoreCase(String category, Pageable pageable);

    Page<Question> findAllByDifficultyLevelIgnoreCase(String difficultyLevel, Pageable pageable);

    Page<Question> findAllByCategoryIgnoreCaseAndDifficultyLevelIgnoreCase(String category, String difficultyLevel, Pageable pageable);

    Page<Question> findAllByQuestionTitleContainingIgnoreCase(String questionTitle, Pageable pageable);
}
