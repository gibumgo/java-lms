package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Question {
    public static final String QUESTION_DELETE_ERROR_MESSAGE = "질문을 삭제할 권한이 없습니다.";
    private Long id;

    private String title;

    private String contents;

    private User writer;

    private Answers answers = new Answers();

    private boolean deleted = false;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate;

    public Question() {
    }

    public Question(User writer, String title, String contents) {
        this(0L, writer, title, contents);
    }

    public Question(Long id, User writer, String title, String contents) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Question setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public Question setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public User getWriter() {
        return writer;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public void delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(QUESTION_DELETE_ERROR_MESSAGE);
        }
        this.deleted = true;
        answers.deleteAnswers(loginUser);
    }

    public List<DeleteHistory> toDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(toDeleteHistory());

        answers.toDeleteHistories(deleteHistories);
        return deleteHistories;
    }

    private DeleteHistory toDeleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, id, getWriter(), LocalDateTime.now());
    }

    public boolean isOwner(User loginUser) {
        return writer.equals(loginUser);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Answer> getAnswers() {
        return this.answers.getAnswers();
    }

    @Override
    public String toString() {
        return "Question [id=" + getId() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }
}
