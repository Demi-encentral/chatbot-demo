package com.esl.lib.ChatGPTDemo.services;

import com.esl.lib.ChatGPTDemo.ChatGptDemoApplication;
import com.esl.lib.ChatGPTDemo.entities.*;
import com.esl.lib.ChatGPTDemo.util.JPAApi;
import com.opencsv.CSVWriter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

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

    public <T> void writeCsv(List<T> entities, File outputFile) {
        if (entities.isEmpty()) {
            // No entities to write, handle accordingly
            return;
        }

        Class<?> entityClass = entities.get(0).getClass();

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile))) {
            // Using reflection to get field names dynamically
            Field[] fields = entityClass.getDeclaredFields();

            // Writing CSV header
            String[] header = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                header[i] = fields[i].getName();
            }
            csvWriter.writeNext(header);

            // Writing CSV data
            for (T entity : entities) {
                String[] entityData = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(entity);
                    entityData[i] = (value != null) ? value.toString() : "";
                }
                csvWriter.writeNext(entityData);
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public File createCsvFileWithDate() throws IOException {
        // Generate a unique file name by appending the current date
        String baseFileName = "knowledge_store";
        Date now = new Date();
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(now);
        String fileNameWithDate = baseFileName + "_" + formattedDate + ".csv";

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
        //12 AM the previous day
        ZonedDateTime now = ZonedDateTime.of(LocalDate.now().minusDays(1).atStartOfDay(), ZoneId.systemDefault());

        List<JpaUser> users = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_USER)
                .where(Q_JPA_USER.dateCreated.after(now).or(Q_JPA_USER.dateModified.after(now)))
                .fetch();

        List<JpaDVD> dvds = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_DVD)
                .where(Q_JPA_DVD.dateCreated.after(now).or(Q_JPA_DVD.dateModified.after(now)))
                .fetch();

        List<JpaGenre> genres = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_GENRE)
                .where(Q_JPA_GENRE.dateCreated.after(now).or(Q_JPA_GENRE.dateModified.after(now)))
                .fetch();

        List<JpaRental> rentals = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_RENTAL)
                .where(Q_JPA_RENTAL.dateCreated.after(now).or(Q_JPA_RENTAL.dateModified.after(now)))
                .fetch();

        List<JpaReview> reviews = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_REVIEW)
                .where(Q_JPA_REVIEW.dateCreated.after(now).or(Q_JPA_REVIEW.dateModified.after(now)))
                .fetch();
      try {
          File file = this.createCsvFileWithDate();


          writeCsv(users, file);
          writeCsv(dvds, file);
          writeCsv(genres, file);
          writeCsv(rentals, file);
          writeCsv(reviews, file);

          return file.getAbsolutePath();
      }
      catch(IOException e) {
          //Log error or something
          System.out.println("Operation failed");
          return null;
      }
    }

    public Path toPath(String fileName) {
        File file = new File(fileName);
        return Paths.get(file.toURI());
    }

    public  void deleteFile(String fileName) {
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
