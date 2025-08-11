package online.ahassan.quizmonoapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ahassan.quizmonoapp.dto.QuestionDto;
import online.ahassan.quizmonoapp.services.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<Page<QuestionDto>> getAllQuestions(@RequestParam Map<String, String> requestFilters, Pageable pageable) {
        Map<String, String> caseInsensitiveFilters = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        caseInsensitiveFilters.putAll(requestFilters);

        try {
            Page<QuestionDto> allQuestions = questionService.getAllQuestions(caseInsensitiveFilters, pageable);
            log.info("Questions fetched successfully. Page: {}, Size: {}, Total: {}",
                    pageable.getPageNumber(), pageable.getPageSize(), allQuestions.getTotalElements());
            return ResponseEntity.ok(allQuestions);
        } catch (Exception e) {
            log.error("Error fetching questions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody QuestionDto questionDto) {
        try {
            String message = questionService.addQuestion(questionDto);
            log.info("Question added successfully: {}", message);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.error("Error adding question", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add question");
        }
    }

    /**
     * PUT - Full update (replace entire resource)
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDto> replaceQuestion(@PathVariable Integer id, @RequestBody QuestionDto questionDto) {
        try {
            QuestionDto updatedQuestion = questionService.replaceQuestion(id, questionDto);
            return ResponseEntity.ok(updatedQuestion);
        } catch (IllegalArgumentException e) {
            log.error("Error replacing question", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error replacing question", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PATCH - Partial update
     */
    @PatchMapping("/{id}")
    public ResponseEntity<QuestionDto> updateQuestion(@PathVariable Integer id, @RequestBody QuestionDto questionDto) {
        try {
            QuestionDto updatedQuestion = questionService.updateQuestion(id, questionDto);
            return ResponseEntity.ok(updatedQuestion);
        } catch (IllegalArgumentException e) {
            log.error("Error updating question", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error updating question", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer id) {
        try {
            String message = questionService.deleteQuestion(id);
            log.info("Question deleted successfully: {}", message);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.error("Error deleting question", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete question");
        }
    }
}
