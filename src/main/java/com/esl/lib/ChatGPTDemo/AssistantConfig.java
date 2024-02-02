package com.esl.lib.ChatGPTDemo;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.SQLException;

@Configuration
@EnableAutoConfiguration
public class AssistantConfig {

    @Value("${chat.api.key}")
    private String apiKey;

    @Bean
    @Primary
    @Qualifier("defaultModel")
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }


    @Bean
    @Qualifier("otherModel")
    public EmbeddingModel otherEmbeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    @Qualifier("defaultStore")
    @Primary
    public EmbeddingStore<TextSegment> embeddingStore() {
        //return new PgVectorEmbeddingStore(dataSource(), "v1", 384, true, 0, true, true);
        return com.esl.lib.ChatGPTDemo.PgVectorEmbeddingStore.builder()
                .host("localhost")
                .table("v1")
                .database("vector")
                .user("postgres")
                .password("postgres")
                .port(5432)
                .dimension(384)
                .build();
    }

    @Bean
    @Qualifier("newStore")
    public EmbeddingStore<TextSegment> newEmbeddingStore() {
        //return new PgVectorEmbeddingStore(dataSource(), "v2", 384, true, 0, true, true);
        return com.esl.lib.ChatGPTDemo.PgVectorEmbeddingStore.builder()
                .host("localhost")
                .table("v2")
                .database("vector")
                .user("postgres")
                .password("postgres")
                .port(5432)
                .dimension(384)
                .build();
    }

    @Bean
    @Primary
    @Qualifier("defaultIngestor")
    public EmbeddingStoreIngestor embeddingStoreIngestor() throws SQLException {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel())
                .embeddingStore(embeddingStore())
                .build();
    }

    @Bean
    @Qualifier("otherIngestor")
    public EmbeddingStoreIngestor otherStoreIngestor() throws SQLException {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(otherEmbeddingModel())
                .embeddingStore(newEmbeddingStore())
                .build();
    }

    @Bean
    @Qualifier("defaultRetrievalChain")
    public ConversationalRetrievalChain conversationalRetrievalChain() throws SQLException {
        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(OpenAiChatModel.withApiKey(apiKey))
                .retriever(EmbeddingStoreRetriever.from(embeddingStore(), embeddingModel()))
                .build();
    }

    @Bean
    @Qualifier("otherRetrievalChain")
    public ConversationalRetrievalChain otherConversationalRetrievalChain() throws SQLException {
        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(OpenAiChatModel.withApiKey(apiKey))
                .retriever(EmbeddingStoreRetriever.from(newEmbeddingStore(), otherEmbeddingModel()))
                .build();
    }

}