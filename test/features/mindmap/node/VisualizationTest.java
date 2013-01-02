package features.mindmap.node;

import base.DocearHttpTest;
import com.google.common.base.Predicate;
import org.junit.Test;
import play.libs.F;
import play.test.TestBrowser;

import javax.annotation.Nullable;

import static java.util.concurrent.TimeUnit.SECONDS;

public class VisualizationTest extends DocearHttpTest {

    @Test
    public void testText() throws Exception {
        runInBrowser(new F.Callback<TestBrowser>() {
            @Override
            @SuppressWarnings("unchecked") //type system is in fluentlenium library broken
            public void invoke(final TestBrowser testBrowser) throws Throwable {
                testBrowser.goTo(url("/"));
                testBrowser.$("#load-map-3").click();
                testBrowser.await().atMost(defaultWait(), SECONDS).until(new Predicate() {
                    @Override
                    public boolean apply(@Nullable Object o) {
                        return testBrowser.pageSource().indexOf("What is Docear") > 0;
                    }
                });
            }
        });
    }
}