package online.ahassan.quizmonoapp.dto;

import lombok.Data;
import online.ahassan.quizmonoapp.entities.QuestionOptions;

import java.io.Serializable;

/**
 * DTO for {@link QuestionOptions}
 */
@Data
public class QuestionOptionsDto implements Serializable {
    private Integer id;
    private String option;
    private Boolean rightAnswer;


    public static QuestionOptionsDto fromEntity(QuestionOptions questionOptions) {
        QuestionOptionsDto questionOptionsDto = new QuestionOptionsDto();
        questionOptionsDto.setId(questionOptions.getId());
        questionOptionsDto.setOption(questionOptions.getOption());
        questionOptionsDto.setRightAnswer(questionOptions.isRightAnswer());
        return questionOptionsDto;
    }

    public QuestionOptions toEntity() {
        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setId(id);
        questionOptions.setOption(option);
        questionOptions.setRightAnswer(rightAnswer);
        return questionOptions;
    }
}