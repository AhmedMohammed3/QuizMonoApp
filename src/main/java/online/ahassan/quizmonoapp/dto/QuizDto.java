package online.ahassan.quizmonoapp.dto;

import lombok.Data;
import online.ahassan.quizmonoapp.entities.Quiz;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Quiz}
 */
@Data
public class QuizDto implements Serializable {
    private Integer id;
    private String title;
    private String category;
    private List<QuestionDto> questions;
    private Integer numOfQuestions;

    public static QuizDto fromEntityMetadata(Quiz quiz) {
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setCategory(quiz.getCategory());
        dto.setNumOfQuestions(quiz.getQuestions() != null ? quiz.getQuestions().size() : 0);
        dto.setQuestions(null); // no questions for metadata
        return dto;
    }

    public static QuizDto fromEntity(Quiz quiz) {
        QuizDto dto = fromEntityMetadata(quiz);
        dto.setQuestions(quiz.getQuestions().stream().map(QuestionDto::fromEntity).toList());
        return dto;
    }

    public Quiz toEntity() {
        Quiz quiz = new Quiz();
        quiz.setId(id);
        quiz.setTitle(title);
        quiz.setCategory(category);
        if (questions != null) {
            quiz.setQuestions(questions.stream().map(QuestionDto::toEntity).toList());
        }
        return quiz;
    }
}