package com.esl.lib.ChatGPTDemo.entities;


import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "genres")
public class JpaGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "genre_name")
    private String genreName;

    @OneToMany(mappedBy = "genre")
    private List<JpaDVD> dvds;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateCreated;

    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateModified;

    public JpaGenre(){}

    public JpaGenre(Long genreId) {
        this.genreId = genreId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public List<JpaDVD> getDvds() {
        return dvds;
    }

    public void setDvds(List<JpaDVD> dvds) {
        this.dvds = dvds;
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
        if (!(o instanceof JpaGenre jpaGenre)) return false;
        return getGenreId().equals(jpaGenre.getGenreId()) && getGenreName().equals(jpaGenre.getGenreName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGenreId(), getGenreName());
    }

    @Override
    public String toString() {
        return "JpaGenre{" +
                "genreId=" + genreId +
                ", genreName='" + genreName + '\'' +
                '}';
    }
}

