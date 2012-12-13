package base;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public abstract class DocearHttpTest {
    /* maybe this could come from a configuration or environment variable, so leave it as function */
    protected static final int port() {
        return 3333;
    }

    protected final void runInServer(final Runnable runnable) {
        running(testServer(port()), runnable);
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
