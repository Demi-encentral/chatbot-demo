package com.esl.lib.ChatGPTDemo;

import com.esl.lib.ChatGPTDemo.services.DataExportService;
import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatController {
    private final String postPrompt = ". Remember, don’t give information not mentioned in the CONTEXT INFORMATION. If a question is out of your knowledge, politely refuse.";

    private HttpServletRequest request;


    private final EmbeddingStoreIngestor v1Ingestor;

    private final ConversationalRetrievalChain v1RetrievalChain;

    private final ConversationalRetrievalChain v2RetrievalChain;

    private final EmbeddingModel v1Model;

    private final EmbeddingModel v2Model;

    private final EmbeddingStoreIngestor v2ingestor;

    private final EmbeddingStore<TextSegment> v1Store;

    private final EmbeddingStore<TextSegment> v2Store;


    private final DataExportService dataExportService;

    @Value("${upload.dir}")
    private String uploadDir;

    public ChatController(HttpServletRequest request, @Qualifier("defaultIngestor") EmbeddingStoreIngestor v1Ingestor, @Qualifier("defaultRetrievalChain") ConversationalRetrievalChain v1RetrievalChain, @Qualifier("otherRetrievalChain") ConversationalRetrievalChain v2RetrievalChain, @Qualifier("defaultModel") EmbeddingModel v1Model, @Qualifier("otherModel") EmbeddingModel v2Model, @Qualifier("otherIngestor") EmbeddingStoreIngestor v2ingestor,
                          @Qualifier("defaultStore") EmbeddingStore<TextSegment> v1Store, @Qualifier("newStore") EmbeddingStore<TextSegment> v2Store, DataExportService dataExportService) {
        this.request = request;
        this.v1Ingestor = v1Ingestor;
        this.v1Model = v1Model;
        this.v2Model = v2Model;
        this.v2ingestor = v2ingestor;
        this.v1RetrievalChain = v1RetrievalChain;
        this.v2RetrievalChain = v2RetrievalChain;
        this.v1Store = v1Store;
        this.v2Store = v2Store;
        this.dataExportService = dataExportService;
        String init = "This is a new session. Only consider the data given. Do not make any mention of 'the given information', .Your primary function is to analyze the data given and return a response. Also, try to sound less robotic. You are a customer care representative dealing with 3rd party users. Try to be as human as possible";
        v1RetrievalChain.execute(init);
        v1RetrievalChain.execute("Refer to yourself as V1.");
        v2RetrievalChain.execute(init);
        v2RetrievalChain.execute("Refer to yourself as V2");
    }

    @PostMapping("/v1")
    public String chatWithDefaultChain(@RequestBody String text) {
        String prompt = text + postPrompt;

        var answer = v1RetrievalChain.execute(prompt);
        return answer;
    }

    @PostMapping("/v2")
    public String chatWithOtherChain(@RequestBody String text) {
        //Has DVD Rental info and the uploaded files
        String prompt = text + postPrompt;
        var answer = v2RetrievalChain.execute(prompt);
        return answer;
    }

    @PostMapping("/v1/init")
    public ResponseEntity<String> initializeV1() {
        String knowledgeStore = dataExportService.exportData();
        Path filePath = new File(knowledgeStore).toPath();
        Document document = FileSystemDocumentLoader.loadDocument(filePath, new TextDocumentParser());
        v1Ingestor.ingest(document);
        dataExportService.deleteFile(knowledgeStore);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/v2/init")
    public ResponseEntity<String> initializeV2() {
        Path guidelinesFilepath = new File("C:\\Users\\D4C\\Desktop\\encentral\\ChatGPTDemo\\src\\main\\resources\\guidelines.pdf").toPath();
        Document guidelinesDoc = FileSystemDocumentLoader.loadDocument(guidelinesFilepath, new ApachePdfBoxDocumentParser());
        v2ingestor.ingest(guidelinesDoc);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/v1/upload")
    public ResponseEntity<String> handleV1Upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }
        Path destination = Paths.get(".").resolve(file.getName()).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destination);
        Document document = loadDocument(destination.toAbsolutePath(), new TextDocumentParser());
        v1Ingestor.ingest(document);
        new File(destination.toUri()).delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/v2/upload")
    public ResponseEntity<String> handleV2Upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }
        Path destination = Paths.get(".").resolve(file.getName()).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destination);
        Document document = loadDocument(destination.toAbsolutePath(), new TextDocumentParser());
        v2ingestor.ingest(document);
        new File(destination.toUri()).delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/v1/ingest")
    public ResponseEntity<String> ingestV1(@RequestBody String text) {
        TextSegment textSegment = TextSegment.from(text);
        Embedding embeddings = v1Model.embed(textSegment).content();
        v1Store.add(embeddings, textSegment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/v2/ingest")
    public ResponseEntity<String> ingestV2(@RequestBody String text) {
        TextSegment textSegment = TextSegment.from(text);
        Embedding embeddings = v2Model.embed(textSegment).content();
        v2Store.add(embeddings, textSegment);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}