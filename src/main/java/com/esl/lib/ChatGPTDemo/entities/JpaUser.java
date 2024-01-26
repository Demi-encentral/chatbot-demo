package com.esl.lib.ChatGPTDemo.entities;


import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class JpaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String username;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<JpaDVD> dvds;

    @OneToMany(mappedBy = "user")
    private List<JpaRental> rentals;

    @OneToMany(mappedBy = "user")
    private List<JpaReview> reviews;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateCreated;

    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateModified;

    public JpaUser() {
    }

    public JpaUser(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<JpaDVD> getDvds() {
        return dvds;
    }

    public void setDvds(List<JpaDVD> dvds) {
        this.dvds = dvds;
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
        if (!(o instanceof JpaUser jpaUser)) return false;
        return Objects.equals(getUserId(), jpaUser.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    @Override
    public String toString() {
        return "JpaUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
