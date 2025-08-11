package online.ahassan.quizmonoapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ahassan.quizmonoapp.dto.QuestionDto;
import online.ahassan.quizmonoapp.dto.QuizDto;
import online.ahassan.quizmonoapp.dto.QuizResponse;
import online.ahassan.quizmonoapp.dto.SubmissionQuestionDto;
import online.ahassan.quizmonoapp.entities.Question;
import online.ahassan.quizmonoapp.entities.Quiz;
import online.ahassan.quizmonoapp.repositories.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final QuizRepository quizRepository;

    private final QuestionService questionService;

    public QuizDto createQuiz(QuizDto quizDto) {
        log.info("Creating new quiz: {}", quizDto.getTitle());
        log.debug("Quiz category: {}, Number of questions requested: {}", quizDto.getCategory(), quizDto.getNumOfQuestions());

        List<QuestionDto> questions = questionService.findRandomQuestionsByCategory(
                quizDto.getCategory(), quizDto.getNumOfQuestions());
        log.debug("Retrieved {} questions for quiz creation", questions.size());

        quizDto.setQuestions(questions);
        Quiz quizEntity = quizDto.toEntity();
        log.debug("Converted QuizDto to Quiz entity: {}", quizEntity);

        Quiz savedQuiz = quizRepository.save(quizEntity);
        log.info("New quiz saved successfully with id={}", savedQuiz.getId());

        QuizDto savedDto = QuizDto.fromEntity(savedQuiz);
        log.debug("Returning created quiz DTO: {}", savedDto);
        return savedDto;
    }

    public QuizDto getQuiz(Integer id) {
        log.info("Fetching metadata for quiz with id={}", id);

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Quiz not found with id={}", id);
                    return new IllegalArgumentException("Quiz not found with id " + id);
                });

        log.debug("Quiz found: {}", quiz);
        QuizDto dto = QuizDto.fromEntityMetadata(quiz);
        log.debug("Returning quiz metadata DTO: {}", dto);
        return dto;
    }

    public Page<QuestionDto> getQuizQuestions(Integer quizId, Pageable pageable) {
        if (!quizRepository.existsById(quizId)) {
            log.warn("Quiz not found with id={}", quizId);
            throw new IllegalArgumentException("Quiz not found with id " + quizId);
        }

        log.debug("Quiz exists, retrieving questions...");
        Page<QuestionDto> questions = questionService.findByQuizId(quizId, pageable);
        log.debug("Fetched {} questions for quizId={}", questions.getTotalElements(), quizId);
        return questions;
    }

    public QuizResponse calculateResult(Integer quizId, List<SubmissionQuestionDto> submissionQuestions) {
        log.info("Calculating quiz result for quizId={} with {} submitted answers",
                quizId, submissionQuestions.size());

        Map<Integer, Question> quizQuestions =
                getQuizQuestions(quizId, Pageable.unpaged()).getContent().stream()
                        .map(QuestionDto::toEntity).toList().stream().collect(Collectors.toMap(Question::getId, q -> q));

        int score = calculateScore(quizId, quizQuestions, submissionQuestions);

        return buildQuizResponse(quizId, score, quizQuestions.size());
    }

    private int calculateScore(Integer quizId, Map<Integer, Question> quizQuestions,
                               List<SubmissionQuestionDto> submissions) {

        return (int) submissions.stream()
                .peek(submitted -> log.debug("Processing submission - questionId={}, selectedOptionId={}",
                        submitted.getQuestionId(), submitted.getSelectedOptionId()))
                .filter(submitted -> {
                    if (!quizQuestions.containsKey(submitted.getQuestionId())) {
                        log.warn("Skipping questionId={} because it does not belong to quizId={}",
                                submitted.getQuestionId(), quizId);
                        return false;
                    }
                    return true;
                })
                .filter(submitted -> questionService.findByQuestionIdAndOptionId(
                                submitted.getQuestionId(), submitted.getSelectedOptionId())
                        .map(option -> {
                            log.debug("Found option for questionId={}, rightAnswer={}",
                                    submitted.getQuestionId(), option.getRightAnswer());
                            if (Boolean.TRUE.equals(option.getRightAnswer())) {
                                log.debug("Correct answer!");
                                return true;
                            } else {
                                log.debug("Incorrect answer.");
                                return false;
                            }
                        })
                        .orElseGet(() -> {
                            log.warn("Option not found for questionId={} and optionId={}",
                                    submitted.getQuestionId(), submitted.getSelectedOptionId());
                            return false;
                        })
                )
                .count();
    }

    private QuizResponse buildQuizResponse(Integer quizId, int score, int totalQuestions) {
        QuizResponse response = new QuizResponse();
        response.setQuizId(quizId);
        response.setTotalMarks(score);
        response.setNumberOfQuestions(totalQuestions);
        log.info("Quiz result calculated: {} out of {} correct", score, totalQuestions);
        return response;
    }

}
