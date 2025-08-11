package online.ahassan.quizmonoapp.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ahassan.quizmonoapp.dto.QuestionDto;
import online.ahassan.quizmonoapp.dto.QuestionOptionsDto;
import online.ahassan.quizmonoapp.entities.Question;
import online.ahassan.quizmonoapp.entities.QuestionOptions;
import online.ahassan.quizmonoapp.repositories.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<QuestionDto> getAllQuestions(Map<String, String> requestFilters, Pageable pageable) {
        log.debug("Fetching questions with filters: {} and pageable: {}", requestFilters, pageable);

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
            log.info("Fetching by category='{}' AND difficulty='{}'", category, difficultyLevel);
            result = questionRepository.findAllByCategoryIgnoreCaseAndDifficultyLevelIgnoreCase(category, difficultyLevel, pageable);
        } else if (category != null) {
            log.info("Fetching by category='{}'", category);
            result = questionRepository.findAllByCategoryIgnoreCase(category, pageable);
        } else if (difficultyLevel != null) {
            log.info("Fetching by difficulty='{}'", difficultyLevel);
            result = questionRepository.findAllByDifficultyLevelIgnoreCase(difficultyLevel, pageable);
        } else if (questionTitle != null) {
            log.info("Fetching by question title containing '{}'", questionTitle);
            result = questionRepository.findAllByQuestionTitleContainingIgnoreCase(questionTitle, pageable);
        } else {
            log.info("Fetching all questions without filters");
            result = questionRepository.findAll(pageable);
        }

        log.debug("Fetched {} questions", result.getTotalElements());
        return result.map(QuestionDto::fromEntity);
    }

    public QuestionDto addQuestion(QuestionDto questionDto) {
        log.info("Adding new question: {}", questionDto.getQuestionTitle());
        Question createdQuestion = questionRepository.save(questionDto.toEntity());
        return QuestionDto.fromEntity(createdQuestion);
    }

    /**
     * PUT - Replace entire Question (must send full payload)
     */
    @Transactional
    public QuestionDto replaceQuestion(Integer id, QuestionDto newDto) {
        log.info("Replacing question with id={} with new data", id);
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Question not found with id: {}", id);
                    return new IllegalArgumentException("Question not found with id: " + id);
                });

        existingQuestion.setQuestionTitle(newDto.getQuestionTitle());
        existingQuestion.setDifficultyLevel(newDto.getDifficultyLevel());
        existingQuestion.setCategory(newDto.getCategory());

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

        Question updatedQuestion = questionRepository.save(existingQuestion);
        log.debug("Question with id={} replaced successfully", id);
        return QuestionDto.fromEntity(updatedQuestion);
    }

    /**
     * PATCH - Partial update (only provided fields updated)
     */
    @Transactional
    public QuestionDto updateQuestion(Integer id, QuestionDto updatedDto) {
        log.info("Partially updating question with id={}", id);
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Question not found with id: {}", id);
                    return new IllegalArgumentException("Question not found with id: " + id);
                });

        if (updatedDto.getQuestionTitle() != null) {
            log.debug("Updating question title to '{}'", updatedDto.getQuestionTitle());
            existingQuestion.setQuestionTitle(updatedDto.getQuestionTitle());
        }
        if (updatedDto.getDifficultyLevel() != null) {
            log.debug("Updating difficulty level to '{}'", updatedDto.getDifficultyLevel());
            existingQuestion.setDifficultyLevel(updatedDto.getDifficultyLevel());
        }
        if (updatedDto.getCategory() != null) {
            log.debug("Updating category to '{}'", updatedDto.getCategory());
            existingQuestion.setCategory(updatedDto.getCategory());
        }

        if (updatedDto.getOptions() != null && !updatedDto.getOptions().isEmpty()) {
            log.debug("Updating options for question id={}", id);
            Map<Integer, QuestionOptions> existingOptionsMap = existingQuestion.getOptions().stream()
                    .collect(Collectors.toMap(QuestionOptions::getId, o -> o));

            List<QuestionOptions> updatedOptions = new ArrayList<>();
            for (QuestionOptionsDto optionDto : updatedDto.getOptions()) {
                if (optionDto.getId() != null && existingOptionsMap.containsKey(optionDto.getId())) {
                    log.debug("Updating existing option id={}", optionDto.getId());
                    QuestionOptions existingOption = existingOptionsMap.get(optionDto.getId());
                    if (optionDto.getOption() != null) {
                        existingOption.setOption(optionDto.getOption());
                    }
                    existingOption.setRightAnswer(optionDto.getRightAnswer());
                    updatedOptions.add(existingOption);
                } else {
                    log.debug("Adding new option");
                    QuestionOptions newOption = optionDto.toEntity();
                    newOption.setQuestion(existingQuestion);
                    updatedOptions.add(newOption);
                }
            }
            existingQuestion.getOptions().clear();
            existingQuestion.getOptions().addAll(updatedOptions);
        }

        Question updatedQuestion = questionRepository.save(existingQuestion);
        log.debug("Partial update for question id={} completed successfully", id);
        return QuestionDto.fromEntity(updatedQuestion);
    }

    @Transactional
    public String deleteQuestion(Integer id) {
        log.info("Deleting question with id={}", id);
        if (!questionRepository.existsById(id)) {
            log.warn("Question not found with id={}", id);
            throw new RuntimeException("Question not found with id " + id);
        }
        questionRepository.deleteById(id);
        log.debug("Question with id={} deleted successfully", id);
        return "Question deleted successfully";
    }
}
