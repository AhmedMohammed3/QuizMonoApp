package online.ahassan.quizmonoapp.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubmissionQuestionDto implements Serializable {
    private Integer questionId;
    private Integer selectedOptionId;
}
