package com.esl.lib.ChatGPTDemo.entities;


import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dvds")
public class JpaDVD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dvd_id")
    private Long dvdId;

    private String title;

    @Column(name = "release_year")
    private Integer releaseYear;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JpaUser user;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private JpaGenre genre;

    @OneToMany(mappedBy = "dvd")
    private List<JpaRental> rentals;

    @OneToMany(mappedBy = "dvd")
    private List<JpaReview> reviews;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateCreated;

    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateModified;

    public JpaDVD() {
    }

    public JpaDVD(Long dvdId) {
        this.dvdId = dvdId;
    }

    public Long getDvdId() {
        return dvdId;
    }

    public void setDvdId(Long dvdId) {
        this.dvdId = dvdId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public JpaUser getUser() {
        return user;
    }

    public void setUser(JpaUser user) {
        this.user = user;
    }

    public JpaGenre getGenre() {
        return genre;
    }

    public void setGenre(JpaGenre genre) {
        this.genre = genre;
    }

    public List<JpaRental> getRentals() {
        return rentals;
    }

    public void setRentals(List<JpaRental> rentals) {
        this.rentals = rentals;
    }

    public List<JpaReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<JpaReview> reviews) {
        this.reviews = reviews;
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
        if (!(o instanceof JpaDVD jpaDVD)) return false;
        return getDvdId().equals(jpaDVD.getDvdId()) && getTitle().equals(jpaDVD.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDvdId(), getTitle());
    }

    @Override
    public String toString() {
        return "JpaDVD{" +
                "dvdId=" + dvdId +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}
