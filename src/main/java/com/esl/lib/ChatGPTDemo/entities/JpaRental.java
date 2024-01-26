package com.esl.lib.ChatGPTDemo.entities;


import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "rentals")
public class JpaRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long rentalId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JpaUser user;

    @ManyToOne
    @JoinColumn(name = "dvd_id")
    private JpaDVD dvd;

    @Column(name = "rental_date")
    private ZonedDateTime rentalDate;

    @Column(name = "return_date")
    private ZonedDateTime returnDate;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateCreated;

    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime dateModified;

    public JpaRental(){}

    public JpaRental(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
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

    public ZonedDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(ZonedDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    public ZonedDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
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
        if (!(o instanceof JpaRental jpaRental)) return false;
        return getRentalId().equals(jpaRental.getRentalId()) && getUser().equals(jpaRental.getUser()) && getDvd().equals(jpaRental.getDvd()) && getRentalDate().equals(jpaRental.getRentalDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRentalId(), getUser(), getDvd(), getRentalDate());
    }

    @Override
    public String toString() {
        return "JpaRental{" +
                "rentalId=" + rentalId +
                ", user=" + user +
                ", dvd=" + dvd +
                '}';
    }
}

