package com.esl.lib.ChatGPTDemo.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class JpaReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JpaUser user;

    @ManyToOne
    @JoinColumn(name = "dvd_id")
    private JpaDVD dvd;

    private int rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateCreated;

    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateModified;

    public JpaReview() {
    }

    public JpaReview(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public JpaUser getUser() {
        return user;
    }

    public void setUser(JpaUser user) {
        this.user = user;
    }

    public JpaDVD getDvd() {
        return dvd;
    }

    public void setDvd(JpaDVD dvd) {
        this.dvd = dvd;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(ZonedDateTime dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaReview jpaReview)) return false;
        return getReviewId().equals(jpaReview.getReviewId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReviewId());
    }

    @Override
    public String toString() {
        return "JpaReview{" +
                "reviewId=" + reviewId +
                ", user=" + user +
                '}';
    }
}
