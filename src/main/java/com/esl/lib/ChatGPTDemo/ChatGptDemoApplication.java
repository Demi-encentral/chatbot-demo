package com.esl.lib.ChatGPTDemo;

import com.esl.lib.ChatGPTDemo.services.DataExportService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@SpringBootApplication
@EnableScheduling
@EntityScan({"com.esl.lib.ChatGPTDemo.entities",})
@ComponentScan({"com.esl.lib.ChatGPTDemo", "com.esl.lib.ChatGPTDemo.services", "com.esl.lib.ChatGPTDemo.util"})
public class ChatGptDemoApplication {

    private final DataExportService dataExportService;

    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    private final EmbeddingStoreIngestor otherEmbeddingStoreIngestor;

    public ChatGptDemoApplication(@Qualifier("defaultIngestor") EmbeddingStoreIngestor embeddingStoreIngestor, DataExportService dataExportService, @Qualifier("otherIngestor") EmbeddingStoreIngestor otherEmbeddingStoreIngestor) {
        this.embeddingStoreIngestor = embeddingStoreIngestor;
        this.dataExportService = dataExportService;
        this.otherEmbeddingStoreIngestor = otherEmbeddingStoreIngestor;
    }

    @PostConstruct
    public void init() {
        //Path restrictions = new File("C:\\Users\\D4C\\Desktop\\encentral\\ChatGPTDemo\\src\\main\\resources\\restrictions.txt").toPath();
        //Document restrictionsDoc = FileSystemDocumentLoader.loadDocument(restrictions, new TextDocumentParser());
        //embeddingStoreIngestor.ingest(restrictionsDoc);
        //otherEmbeddingStoreIngestor.ingest(restrictionsDoc);
    }

    private static Path toPath(String fileName) {
        try {
            URL fileUrl = ChatGptDemoApplication.class.getResource(fileName);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(ChatGptDemoApplication.class, args);
    }
}
