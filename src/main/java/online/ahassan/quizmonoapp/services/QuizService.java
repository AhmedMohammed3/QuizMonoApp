package online.ahassan.quizmonoapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ahassan.quizmonoapp.dto.QuestionDto;
import online.ahassan.quizmonoapp.dto.QuizDto;
import online.ahassan.quizmonoapp.entities.Quiz;
import online.ahassan.quizmonoapp.repositories.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final QuizRepository quizRepository;

    private final QuestionService questionService;

    public QuizDto createQuiz(QuizDto quizDto) {
        log.info("Creating new quiz: {}", quizDto.getTitle());
        List<QuestionDto> questions = questionService.findRandomQuestionsByCategory(quizDto.getCategory(), quizDto.getNumOfQuestions());
        quizDto.setQuestions(questions);
        Quiz quizEntity = quizDto.toEntity();
        Quiz savedQuiz = quizRepository.save(quizEntity);
        log.debug("New quiz created successfully: {}", savedQuiz.getTitle());
        return QuizDto.fromEntity(savedQuiz);
    }

    public QuizDto getQuiz(Integer id) {
        log.info("Fetching metadata for quiz with id={}", id);
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id " + id));
        return QuizDto.fromEntityMetadata(quiz);
    }

    public Page<QuestionDto> getQuizQuestions(Integer quizId, Pageable pageable) {
        log.info("Fetching paginated questions for quizId={}, page={}, size={}",
                quizId, pageable.getPageNumber(), pageable.getPageSize());

        // Ensure quiz exists (optional, remove if not needed)
        if (!quizRepository.existsById(quizId)) {
            throw new IllegalArgumentException("Quiz not found with id " + quizId);
        }

        return questionService.findByQuizId(quizId, pageable);

    }
}
