package com.esl.lib.ChatGPTDemo;

import com.esl.lib.ChatGPTDemo.services.DataExportService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.cassandra.CassandraEmbeddingConfiguration;
import dev.langchain4j.store.embedding.cassandra.CassandraEmbeddingStore;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAutoConfiguration
public class AssistantConfig {

    @Value("${database.token}")
    private String astraToken;

    @Value("${database.id}")
    private String databaseId;

    @Value("${chat.api.key}")
    private String apiKey;

    @Value("${neo4j.uri}")
    private String uri;

    @Value("${neo4j.username}")
    private String username;

    @Value("${neo4j.password}")
    private  String password;

    @Autowired
    DataExportService dataExportService;


    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }


    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(){
        return Neo4jEmbeddingStore.builder()
                .withBasicAuth(uri, username, password)
                .dimension(384)
                .build();
    }


//    @Bean
//    public CassandraEmbeddingStore cassandraEmbeddingStore() {
//        List<String> contactPoints = Arrays.asList("127.0.0.1"); //localhost
//        return new CassandraEmbeddingStore(CassandraEmbeddingConfiguration.builder()
//                .port(9042)
//                .password("cassandra")
//                .userName("cassandra")
//                .keyspace("help")
//                .dimension(384)
//                .table("state")
//                .localDataCenter("datacenter1")
//                .contactPoints(contactPoints)
//                .build());
//    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor() {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel())
                .embeddingStore(embeddingStore())
                .build();
    }

    @Bean
    public ConversationalRetrievalChain conversationalRetrievalChain() {
        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(OpenAiChatModel.withApiKey(apiKey))
                .retriever(EmbeddingStoreRetriever.from(embeddingStore(), embeddingModel()))
                .build();
    }

}