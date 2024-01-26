package com.esl.lib.ChatGPTDemo.util;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Transactional
public class JPAApi {
    @Autowired
    @PersistenceContext
    EntityManager em;

    public JPAApi() {
    }

    public EntityManager em() {
        return this.em;
    }

    public <T> T withTransaction(Function<EntityManager, T> block) {
        return block.apply(this.em);
    }

    public <T> T withTransaction(Supplier<T> block) {
        return block.get();
    }

    public void withTransaction(Runnable block) {
        block.run();
    }
}
