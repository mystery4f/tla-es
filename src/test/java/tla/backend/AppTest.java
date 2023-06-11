package tla.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {EsApp.class})
class AppTest {

    @Test
    void app() {
        EsApp classUnderTest = new EsApp();
        assertTrue(
            classUnderTest.getClass().isAnnotationPresent(
                EnableAutoConfiguration.class
            ),
            "auto conf annotation should be present"
        );
    }

}
