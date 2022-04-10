package com.epam.spsa.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"article", "estimations"})
@Table(name = "article_comment")
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "content", columnDefinition = "CLOB")
    @NonNull
    private String content;

    @Column(name = "creation_date")
    @NonNull
    private Timestamp creationDate;

    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Estimation> estimations = new HashSet<>();

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_comment_reply",
            joinColumns = @JoinColumn(name = "main_article_comment_id"),
            inverseJoinColumns = @JoinColumn(name = "reply_article_comment_id"))
    private List<ArticleComment> articleComments = new ArrayList<>();

    public boolean addEstimation(Estimation e) {
        return estimations.add(e);
    }

    public int getLikes() {
        return (int) estimations.stream()
                .filter(estimation -> estimation.getValue() == 1).count();
    }

    public int getDislikes() {
        return (int) estimations.stream()
                .filter(estimation -> estimation.getValue() == -1).count();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComment that = (ArticleComment) o;
        return id == that.id &&
                Objects.equals(content, that.content) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(estimations, that.estimations) &&
                Objects.equals(image, that.image) &&
                Objects.equals(user, that.user) &&
                Objects.equals(article, that.article) &&
                Objects.equals(articleComments, that.articleComments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, creationDate, image, article, articleComments);
    }
}
