import org.junit.Test;
import play.libs.F;
import play.test.TestBrowser;

import java.util.concurrent.TimeUnit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class QUnitBridge {
    @Test
    public void runInBrowser() {
        running(testServer(3333), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333/qunit");
                final String selectorFailedCounter = "#qunit-testresult .failed";
                browser.await().atMost(4, TimeUnit.SECONDS).until(selectorFailedCounter).hasSize(1);
                assertThat(browser.$(selectorFailedCounter, 0).getText()).overridingErrorMessage("some qunit tests had failed").isEqualTo("0");
            }
        });
    }
}
