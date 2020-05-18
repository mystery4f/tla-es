package tla.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tla.backend.App;
import tla.backend.es.model.AnnotationEntity;
import tla.backend.es.model.LemmaEntity;
import tla.backend.es.model.ThsEntryEntity;

@SpringBootTest(classes = {App.class})
public class ServiceTest {

    @Autowired
    private LemmaService lemmaService;

    @Test
    void testServiceRegistry() {
        assertNotNull(lemmaService, "lemma service should be injected");
        Annotation lemmaServiceModelClassAnnotation = null;
        for (Annotation a : lemmaService.getClass().getAnnotations()) {
            lemmaServiceModelClassAnnotation = (a instanceof ModelClass) ? a : lemmaServiceModelClassAnnotation;
        }
        assertNotNull(lemmaServiceModelClassAnnotation, "lemma service should have expected annotation");
        assertAll("test if services register themselves",
            () -> assertTrue(QueryService.modelClassServices.size() > 0),
            () -> assertTrue(QueryService.modelClassServices.containsKey(LemmaEntity.class)),
            () -> assertTrue(QueryService.modelClassServices.containsKey(ThsEntryEntity.class)),
            () -> assertTrue(QueryService.modelClassServices.containsKey(AnnotationEntity.class)),
            () -> assertTrue(QueryService.getRegisteredModelClasses().size() > 0)
        );
    }
}