package com.epam.spsa.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "author_name")
    @NonNull
    private String authorName;

    @Column(name = "author_surname")
    @NonNull
    private String authorSurname;

    @Column(name = "topic")
    @NonNull
    private String topic;

    @Column(name = "description")
    @NonNull
    private String description;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "creation_date")
    @NonNull
    private LocalDate creationDate;

    @Lob
    @Type(type = "text")
    @Column(name = "content")
    private String content;

    @ManyToMany
    @JoinTable(
            name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    @NonNull
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleComment> articleComments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id
                && Objects.equals(authorName, article.authorName)
                && Objects.equals(authorSurname, article.authorSurname)
                && Objects.equals(topic, article.topic)
                && Objects.equals(description, article.description)
                && Objects.equals(pictureUrl, article.pictureUrl)
                && Objects.equals(creationDate, article.creationDate)
                && Objects.equals(content, article.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
