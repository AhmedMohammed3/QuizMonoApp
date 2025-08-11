package online.ahassan.quizmonoapp.repositories;

import online.ahassan.quizmonoapp.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
