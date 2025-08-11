package online.ahassan.quizmonoapp.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuizResponse implements Serializable {
    private Integer quizId;
    private Integer totalMarks;
    private Integer numberOfQuestions;
}
