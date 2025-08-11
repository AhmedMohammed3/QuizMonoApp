package online.ahassan.quizmonoapp.repositories;

import online.ahassan.quizmonoapp.entities.QuestionOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionsRepository extends JpaRepository<QuestionOptions, Integer> {
}
