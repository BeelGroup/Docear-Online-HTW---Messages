package configuration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import play.Play;
import services.backend.mindmap.MindMapCrudService;
import services.backend.mindmap.MockMindMapCrudService;
import services.backend.mindmap.ServerMindMapCrudService;

import static org.apache.commons.lang.BooleanUtils.isTrue;

@Configuration
@ComponentScan({"controllers", "services"})
public class SpringConfiguration {

    private static AnnotationConfigApplicationContext APPLICATIONCONTEXT;

    public static <T> T getBean(Class<T> beanClass) {
        if (APPLICATIONCONTEXT == null) {
            throw new IllegalStateException("application context is not initialized");
        }
        return APPLICATIONCONTEXT.getBean(beanClass);
    }

    public static AnnotationConfigApplicationContext getApplicationContext() {
        return APPLICATIONCONTEXT;
    }

    public static void setApplicationContext(AnnotationConfigApplicationContext applicationContext) {
        APPLICATIONCONTEXT = applicationContext;
    }

    @Bean
    public MindMapCrudService mindMapCrudService() {
        MindMapCrudService mindMapCrudService;
        final boolean mock = isTrue(Play.application().configuration().getBoolean("backend.mock"));
        if (mock) {
            mindMapCrudService = new MockMindMapCrudService();
        } else {
            mindMapCrudService = new ServerMindMapCrudService();
        }
        return mindMapCrudService;
    }
}