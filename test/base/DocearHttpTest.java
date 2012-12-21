package base;

import com.google.common.collect.Maps;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import play.libs.F;
import play.test.TestBrowser;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static play.test.Helpers.*;

public abstract class DocearHttpTest {
    /* maybe this could come from a configuration or environment variable, so leave it as function */
    protected static final int port() {
        return 3333;
    }

    /** default waiting time in seconds **/
    protected int defaultWait() {
        return 3;
    }

    protected final void runInServer(final Runnable runnable) {
        running(testServer(port()), runnable);
    }

    protected final void runInBrowser(final F.Callback<TestBrowser> callback) {
        running(testServer(port()), driver(), callback);
    }

    private Class<? extends WebDriver> driver() {
        final String driverAlias = System.getProperty("PLAY_WEBDRIVER", "HTMLUNIT");
        final Map<String, Class<? extends WebDriver>> map = newHashMap();
        map.put("FIREFOX", FIREFOX);//play -DPLAY_WEBDRIVER=FIREFOX test
        map.put("CHROME", ChromeDriver.class);
        Class<? extends WebDriver> driver = HTMLUNIT;
        if (map.containsKey(driverAlias)) {
            driver = map.get(driverAlias);
        }
        return driver;
    }

    /**
     * Generates an URL for a test.
     *
     * example: url("/hello/world.html") returns http://localhost:3333/hello/world.html
     *
     * @param path the absolute path of for the URL
     * @return the complete URL with the path.
     */
    protected static final String url(final String path) {
        return "http://localhost:" + port() + path;
    }
}
