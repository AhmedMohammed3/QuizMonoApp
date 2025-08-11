package online.ahassan.quizmonoapp.dto;

import lombok.Data;
import online.ahassan.quizmonoapp.entities.Question;
import online.ahassan.quizmonoapp.entities.QuestionOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto implements Serializable {
    private Integer id;
    private String questionTitle;
    private List<QuestionOptionsDto> options = new ArrayList<>();
    private String difficultyLevel;
    private String category;

    public static QuestionDto fromEntity(Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setQuestionTitle(question.getQuestionTitle());
        questionDto.setOptions(question.getOptions().stream().map(QuestionOptionsDto::fromEntity).toList());
        questionDto.setDifficultyLevel(question.getDifficultyLevel());
        questionDto.setCategory(question.getCategory());
        return questionDto;
    }

    public Question toEntity() {
        Question question = new Question();
        question.setId(id);
        question.setQuestionTitle(questionTitle);
        question.setDifficultyLevel(difficultyLevel);
        question.setCategory(category);

        // Convert options and set the question reference
        List<QuestionOptions> questionOptions = options.stream()
                .map(QuestionOptionsDto::toEntity)
                .peek(option -> option.setQuestion(question))
                .toList();
        question.setOptions(questionOptions);

        return question;
    }
}