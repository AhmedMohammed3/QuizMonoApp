package online.ahassan.quizmonoapp.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import online.ahassan.quizmonoapp.dto.QuestionDto;
import online.ahassan.quizmonoapp.dto.QuestionOptionsDto;
import online.ahassan.quizmonoapp.entities.Question;
import online.ahassan.quizmonoapp.entities.QuestionOptions;
import online.ahassan.quizmonoapp.repositories.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<QuestionDto> getAllQuestions(Map<String, String> requestFilters, Pageable pageable) {
        Map<String, String> filters = requestFilters.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toLowerCase(),
                        Map.Entry::getValue
                ));

        Page<Question> result;

        String category = filters.get("category");
        String difficultyLevel = filters.get("difficulty_level");
        String questionTitle = filters.get("question_title");

        if (category != null && difficultyLevel != null) {
            result = questionRepository.findAllByCategoryIgnoreCaseAndDifficultyLevelIgnoreCase(category, difficultyLevel, pageable);
        } else if (category != null) {
            result = questionRepository.findAllByCategoryIgnoreCase(category, pageable);
        } else if (difficultyLevel != null) {
            result = questionRepository.findAllByDifficultyLevelIgnoreCase(difficultyLevel, pageable);
        } else if (questionTitle != null) {
            result = questionRepository.findAllByQuestionTitleContainingIgnoreCase(questionTitle, pageable);
        } else {
            result = questionRepository.findAll(pageable);
        }

        return result.map(QuestionDto::fromEntity);
    }

    public String addQuestion(QuestionDto questionDto) {
        questionRepository.save(questionDto.toEntity());
        return "Question added successfully";
    }

    /**
     * PUT - Replace entire Question (must send full payload)
     */
    @Transactional
    public String replaceQuestion(Integer id, QuestionDto newDto) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));

        // Replace all fields
        existingQuestion.setQuestionTitle(newDto.getQuestionTitle());
        existingQuestion.setDifficultyLevel(newDto.getDifficultyLevel());
        existingQuestion.setCategory(newDto.getCategory());

        // Replace all options
        existingQuestion.getOptions().clear();
        if (newDto.getOptions() != null) {
            List<QuestionOptions> options = newDto.getOptions().stream()
                    .map(dto -> {
                        QuestionOptions opt = dto.toEntity();
                        opt.setQuestion(existingQuestion);
                        return opt;
                    })
                    .toList();
            existingQuestion.getOptions().addAll(options);
        }

        questionRepository.save(existingQuestion);
        return "Question replaced successfully";
    }

    /**
     * PATCH - Partial update (only provided fields updated)
     */
    @Transactional
    public String updateQuestion(Integer id, QuestionDto updatedDto) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));

        if (updatedDto.getQuestionTitle() != null) {
            existingQuestion.setQuestionTitle(updatedDto.getQuestionTitle());
        }
        if (updatedDto.getDifficultyLevel() != null) {
            existingQuestion.setDifficultyLevel(updatedDto.getDifficultyLevel());
        }
        if (updatedDto.getCategory() != null) {
            existingQuestion.setCategory(updatedDto.getCategory());
        }

        if (updatedDto.getOptions() != null && !updatedDto.getOptions().isEmpty()) {
            Map<Integer, QuestionOptions> existingOptionsMap = existingQuestion.getOptions().stream()
                    .collect(Collectors.toMap(QuestionOptions::getId, o -> o));

            List<QuestionOptions> updatedOptions = new ArrayList<>();
            for (QuestionOptionsDto optionDto : updatedDto.getOptions()) {
                if (optionDto.getId() != null && existingOptionsMap.containsKey(optionDto.getId())) {
                    QuestionOptions existingOption = existingOptionsMap.get(optionDto.getId());
                    if (optionDto.getOption() != null) {
                        existingOption.setOption(optionDto.getOption());
                    }
                    existingOption.setRightAnswer(optionDto.getRightAnswer());
                    updatedOptions.add(existingOption);
                } else {
                    QuestionOptions newOption = optionDto.toEntity();
                    newOption.setQuestion(existingQuestion);
                    updatedOptions.add(newOption);
                }
            }
            existingQuestion.getOptions().clear();
            existingQuestion.getOptions().addAll(updatedOptions);
        }

        questionRepository.save(existingQuestion);
        return "Question updated successfully";
    }

    @Transactional
    public String deleteQuestion(Integer id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id " + id);
        }
        questionRepository.deleteById(id);
        return "Question deleted successfully";
    }
}
