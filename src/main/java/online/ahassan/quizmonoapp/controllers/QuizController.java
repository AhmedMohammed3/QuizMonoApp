package online.ahassan.quizmonoapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ahassan.quizmonoapp.dto.QuestionDto;
import online.ahassan.quizmonoapp.dto.QuizDto;
import online.ahassan.quizmonoapp.services.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizMetadata(@PathVariable Integer id) {
        try {
            QuizDto quiz = quizService.getQuiz(id);
            return ResponseEntity.ok(quiz);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching quiz metadata", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error fetching quiz metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<Page<QuestionDto>> getQuizQuestions(@PathVariable Integer id, Pageable pageable) {
        try {
            Page<QuestionDto> questions = quizService.getQuizQuestions(id, pageable);
            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching quiz questions", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error fetching quiz questions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(@RequestBody QuizDto quizDto) {
        try {
            QuizDto savedQuiz = quizService.createQuiz(quizDto);
            log.info("Quiz created successfully: {}", savedQuiz.getTitle());
            return ResponseEntity.ok(savedQuiz);
        } catch (Exception e) {
            log.error("Error adding quiz", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
