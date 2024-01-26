package com.esl.lib.ChatGPTDemo.services;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
public class RetrievalChainReloader {

    private final ConversationalRetrievalChain retrievalChain;

    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    private final DataExportService dataExportService;

    public RetrievalChainReloader(ConversationalRetrievalChain retrievalChain, DataExportService dataExportService, EmbeddingStoreIngestor embeddingStoreIngestor) {
        this.retrievalChain = retrievalChain;
        this.dataExportService = dataExportService;
        this.embeddingStoreIngestor = embeddingStoreIngestor;
    }

    // Method to periodically write data to a file
//    @Scheduled(cron = "0 0 0 * * ?") // Execute daily at 0:00 AM
    @Scheduled(cron = "0 0/5 * * * ?")
    public void reload(){
        String knowledgeStore = this.dataExportService.exportData();
        Document document = loadDocument(dataExportService.toPath(knowledgeStore), new TextDocumentParser());
        embeddingStoreIngestor.ingest(document);
        dataExportService.deleteFile(knowledgeStore);
    }
}
