package com.esl.lib.ChatGPTDemo.services;

import com.esl.lib.ChatGPTDemo.entities.*;
import com.esl.lib.ChatGPTDemo.util.JPAApi;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DataExportService {

    private final QJpaUser Q_JPA_USER = QJpaUser.jpaUser;

    private final QJpaDVD Q_JPA_DVD = QJpaDVD.jpaDVD;

    private final QJpaGenre Q_JPA_GENRE = QJpaGenre.jpaGenre;

    private final QJpaRental Q_JPA_RENTAL = QJpaRental.jpaRental;

    private final QJpaReview Q_JPA_REVIEW = QJpaReview.jpaReview;

    private JPAApi jpaApi;


    public DataExportService(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public <T> void writeText(String textContent, File outputFile) {
        if (textContent == "") {
            // No content to write, handle accordingly
            return;
        }

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(textContent);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public String writeUsers() {
        List<JpaUser> users = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_USER)
                //.where(Q_JPA_USER.dateCreated.after(now).or(Q_JPA_USER.dateModified.after(now)))
                .fetch();
        List<String> result = new ArrayList<>();
        result = users.stream().map(u -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dvdIds = u.getDvds().stream().map(i->i.getDvdId().toString()).collect(Collectors.joining(", ")).toString();
            String rentalIds = u.getRentals().stream().map(i->i.getRentalId().toString()).collect(Collectors.joining(", ")).toString();
            String reviewIds = u.getReviews().stream().map(i->i.getReviewId().toString()).collect(Collectors.joining(", ")).toString();
            Date dateCreated = Date.from(u.getDateCreated().toInstant());
            Date dateModified = Date.from(u.getDateModified().toInstant());
            String formattedDateCreated = simpleDateFormat.format(dateCreated);
            String formattedDateModified = simpleDateFormat.format(dateModified);
            return String.format(
                    "The user with ID %d, username \"%s,\" and email \"%s\" has DVDs with IDs %s, " +
                            "rentals with IDs %s, and reviews with IDs %s. The user account was created on %s, " +
                            "and it was last modified on %s.",
                    u.getUserId(), u.getUsername(), u.getEmail(),dvdIds,
                    rentalIds,reviewIds,formattedDateCreated,formattedDateModified
            );
        }).collect(Collectors.toList());
        return result.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public String writeDVDs() {
        List<JpaDVD> dvds = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_DVD)
                //.where(Q_JPA_DVD.dateCreated.after(now).or(Q_JPA_DVD.dateModified.after(now)))
                .fetch();
        List<String> result = dvds.stream().map(dvd -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String rentalsIds = dvd.getRentals().stream().map(r -> r.getRentalId().toString()).collect(Collectors.joining(", "));
            String reviewsIds = dvd.getReviews().stream().map(r -> r.getReviewId().toString()).collect(Collectors.joining(", "));
            Date dateCreated = Date.from(dvd.getDateCreated().toInstant());
            Date dateModified = Date.from(dvd.getDateModified().toInstant());
            String formattedDateCreated = simpleDateFormat.format(dateCreated);
            String formattedDateModified = simpleDateFormat.format(dateModified);
            return String.format(
                    "The DVD with ID %d, titled \"%s,\" released in %d, and associated with user ID %d, " +
                            "genre ID %d, has rentals with IDs %s, and reviews with IDs %s. The DVD was created on %s, " +
                            "and it was last modified on %s.",
                    dvd.getDvdId(), dvd.getTitle(), dvd.getReleaseYear(), dvd.getUser().getUserId(), dvd.getGenre().getGenreId(),
                    rentalsIds, reviewsIds, formattedDateCreated, formattedDateModified
            );
        }).collect(Collectors.toList());
        return result.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public String writeGenres() {
        List<JpaGenre> genres = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_GENRE)
                //.where(Q_JPA_GENRE.dateCreated.after(now).or(Q_JPA_GENRE.dateModified.after(now)))
                .fetch();
        List<String> result = genres.stream().map(genre -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dvdsIds = genre.getDvds().stream().map(d -> d.getDvdId().toString()).collect(Collectors.joining(", "));
            Date dateCreated = Date.from(genre.getDateCreated().toInstant());
            Date dateModified = Date.from(genre.getDateModified().toInstant());
            String formattedDateCreated = simpleDateFormat.format(dateCreated);
            String formattedDateModified = simpleDateFormat.format(dateModified);
            return String.format(
                    "The genre with ID %d, named \"%s,\" has DVDs with IDs %s. The genre was created on %s, " +
                            "and it was last modified on %s.",
                    genre.getGenreId(), genre.getGenreName(), dvdsIds, formattedDateCreated, formattedDateModified
            );
        }).collect(Collectors.toList());
        return result.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public String writeRentals() {
        List<JpaRental> rentals = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_RENTAL)
                //.where(Q_JPA_RENTAL.dateCreated.after(now).or(Q_JPA_RENTAL.dateModified.after(now)))
                .fetch();
        List<String> result = rentals.stream().map(rental -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedRentalDate = simpleDateFormat.format(Date.from(rental.getRentalDate().toInstant()));
            String formattedReturnDate = rental.getReturnDate() != null ? simpleDateFormat.format(Date.from(rental.getReturnDate().toInstant())) : "N/A";
            Date dateCreated = Date.from(rental.getDateCreated().toInstant());
            Date dateModified = Date.from(rental.getDateModified().toInstant());
            String formattedDateCreated = simpleDateFormat.format(dateCreated);
            String formattedDateModified = simpleDateFormat.format(dateModified);
            return String.format(
                    "The rental with ID %d, associated with user ID %d and DVD ID %d, " +
                            "was rented on %s and returned on %s. The rental was created on %s, " +
                            "and it was last modified on %s.",
                    rental.getRentalId(), rental.getUser().getUserId(), rental.getDvd().getDvdId(),
                    formattedRentalDate, formattedReturnDate, formattedDateCreated, formattedDateModified
            );
        }).collect(Collectors.toList());
        return result.stream().collect(Collectors.joining(System.lineSeparator()));
    }


    public String writeReviews() {
        List<JpaReview> reviews = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_REVIEW)
                //.where(Q_JPA_REVIEW.dateCreated.after(now).or(Q_JPA_REVIEW.dateModified.after(now)))
                .fetch();
        List<String> result = reviews.stream().map(review -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateCreated = simpleDateFormat.format(Date.from(review.getDateCreated().toInstant()));
            String formattedDateModified = simpleDateFormat.format(Date.from(review.getDateModified().toInstant()));
            return String.format(
                    "The review with ID %d, associated with user ID %d and DVD ID %d, " +
                            "has a rating of %d and the following review text: \"%s\". The review was created on %s, " +
                            "and it was last modified on %s.",
                    review.getReviewId(), review.getUser().getUserId(), review.getDvd().getDvdId(),
                    review.getRating(), review.getReviewText(), formattedDateCreated, formattedDateModified
            );
        }).collect(Collectors.toList());
        return result.stream().collect(Collectors.joining(System.lineSeparator()));
    }



    public File createTextFileWithDate() throws IOException {
        // Generate a unique file name by appending the current date
        String baseFileName = "knowledge_store";
        Date now = new Date();
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(now);
        String fileNameWithDate = baseFileName + "_" + formattedDate + ".txt";

        Resource resource = new ClassPathResource("");

        // Get the absolute path of the resource (the "resources" directory)
        File resourcesDir = resource.getFile();

        File outputFile = new File(resourcesDir, fileNameWithDate);

        try {
            if (outputFile.createNewFile()) {
                System.out.println("File created: " + outputFile.getAbsolutePath());
                return outputFile;
            } else {
                System.out.println("File already exists: " + outputFile.getAbsolutePath());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }

    public String exportData() {

        try {
            File file = this.createTextFileWithDate();
            StringBuilder sb = new StringBuilder();
            sb.append(this.writeUsers());
            sb.append(this.writeDVDs());
            sb.append(this.writeReviews());
            sb.append(this.writeGenres());
            sb.append(this.writeRentals());
            writeText(sb.toString(), file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            //Log error or something
            System.out.println("Operation failed");
            return null;
        }
    }

    public Path toPath(String fileName) {
        File file = new File(fileName);
        return Paths.get(file.toURI());
    }

    public void deleteFile(String fileName) {
        File fileToDelete = new File(fileName);
        if (fileToDelete == null || !fileToDelete.exists()) {
            System.out.println("File does not exist or is already deleted.");
        }
        if (fileToDelete.delete()) {
            System.out.println("File deleted successfully: " + fileToDelete.getAbsolutePath());
        } else {
            System.out.println("Failed to delete the file: " + fileToDelete.getAbsolutePath());
        }
    }
}
