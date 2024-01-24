package com.esl.lib.ChatGPTDemo;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@SpringBootApplication
public class ChatGptDemoApplication {

	@Value("${knowledge.store.pdf}")
	private String knowledgeStore;

	private final EmbeddingStoreIngestor embeddingStoreIngestor;

	public ChatGptDemoApplication(EmbeddingStoreIngestor embeddingStoreIngestor) {
		this.embeddingStoreIngestor = embeddingStoreIngestor;
	}

	@PostConstruct
	public void init() {
		//new ApachePoiDocumentParser() - for Docx
		Document document = loadDocument(toPath(knowledgeStore), new ApachePdfBoxDocumentParser());
		embeddingStoreIngestor.ingest(document);
	}

	private static Path toPath(String fileName) {
		try {
			URL fileUrl = ChatGptDemoApplication.class.getClassLoader().getResource(fileName);
			return Paths.get(fileUrl.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ChatGptDemoApplication.class, args);
	}

}
