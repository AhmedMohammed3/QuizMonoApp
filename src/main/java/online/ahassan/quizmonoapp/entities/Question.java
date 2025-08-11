package online.ahassan.quizmonoapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String questionTitle;
    private String difficultyLevel;
    private String category;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOptions> options = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "quiz_questions", joinColumns = @JoinColumn(name = "questions_id"), inverseJoinColumns = @JoinColumn(name = "quiz_id"))
    private List<Quiz> quiz;
}
