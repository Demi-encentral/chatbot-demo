package com.esl.lib.ChatGPTDemo;

import com.esl.lib.ChatGPTDemo.services.DataExportService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@SpringBootApplication
@EnableScheduling
@EntityScan({"com.esl.lib.ChatGPTDemo.entities",})
@ComponentScan({"com.esl.lib.ChatGPTDemo","com.esl.lib.ChatGPTDemo.services", "com.esl.lib.ChatGPTDemo.util"})
public class ChatGptDemoApplication {

    private final DataExportService dataExportService;

    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    public ChatGptDemoApplication(EmbeddingStoreIngestor embeddingStoreIngestor, DataExportService dataExportService) {
        this.embeddingStoreIngestor = embeddingStoreIngestor;
        this.dataExportService = dataExportService;
    }

    @PostConstruct
    public void init() {
        //new ApachePoiDocumentParser() - for Docx
        String knowledgeStore = dataExportService.exportData();
        Document document = loadDocument(dataExportService.toPath(knowledgeStore), new TextDocumentParser());
        embeddingStoreIngestor.ingest(document);
        dataExportService.deleteFile(knowledgeStore);
    }


    public static void main(String[] args) {
        SpringApplication.run(ChatGptDemoApplication.class, args);
    }

}
