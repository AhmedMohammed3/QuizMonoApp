package online.ahassan.quizmonoapp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import online.ahassan.quizmonoapp.entities.QuestionOptions;

/**
 * DTO for {@link QuestionOptions}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionOptionDto extends QuestionOptionAdminDto {
    private Boolean rightAnswer;

    public static QuestionOptionDto fromEntity(QuestionOptions questionOptions) {
        QuestionOptionDto questionOptionDto = new QuestionOptionDto();
        questionOptionDto.setId(questionOptions.getId());
        questionOptionDto.setOption(questionOptions.getOption());
        questionOptionDto.setRightAnswer(questionOptions.getRightAnswer());
        return questionOptionDto;
    }

    public QuestionOptions toEntity() {
        QuestionOptions questionOptions = super.toEntity();
        questionOptions.setRightAnswer(rightAnswer);
        return questionOptions;
    }
}