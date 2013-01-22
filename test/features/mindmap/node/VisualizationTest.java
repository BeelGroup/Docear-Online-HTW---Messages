package features.mindmap.node;

import base.DocearHttpTest;
import com.google.common.base.Predicate;
import org.junit.Test;
import play.libs.F;
import play.test.TestBrowser;

import javax.annotation.Nullable;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fest.assertions.Assertions.assertThat;

public class VisualizationTest extends DocearHttpTest {

    public static final String LOAD_MAP_3 = "#load-map-2";

    @Test
    public void testText() throws Exception {
//        runInBrowser(new F.Callback<TestBrowser>() {
//            @Override
//            @SuppressWarnings("unchecked") //type system is in fluentlenium library broken
//            public void invoke(final TestBrowser testBrowser) throws Throwable {
//                testBrowser.goTo(url("/"));
//                testBrowser.executeScript(format("$('%s').click()", LOAD_MAP_3));//@hack defaultWait()
//                testBrowser.await().atMost(15, SECONDS).until(new Predicate() {
//                    @Override
//                    public boolean apply(@Nullable Object o) {
//                        return testBrowser.pageSource().indexOf("What is Docear") > 0;
//                    }
//                });
//            }
//        });
    }
}
