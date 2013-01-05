package models.frontend;

import play.mvc.Http;

public class LoggedError {
    private Http.RequestHeader requestHeader;
    private Throwable throwable;

    public LoggedError(Http.RequestHeader requestHeader, Throwable throwable) {
        this.requestHeader = requestHeader;
        this.throwable = throwable;
    }

    public Http.RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
