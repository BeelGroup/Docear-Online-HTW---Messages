import info.schleichardt.play2.basicauth.CredentialsFromConfChecker;
import info.schleichardt.play2.basicauth.JAuthenticator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.api.mvc.Handler;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

import static org.apache.commons.lang.BooleanUtils.isTrue;

public class Global extends GlobalSettings {
    private final JAuthenticator authenticator = new JAuthenticator(new CredentialsFromConfChecker());
    private boolean basicAuthEnabled;

    @Override
    public void onStart(Application application) {
        Logger.info("using configuration " + Play.application().configuration().getString("application.conf.name"));
        initializeSpring();
        initializeBasicAuthPlugin();
        super.onStart(application);
    }

    @Override
    public Handler onRouteRequest(final Http.RequestHeader requestHeader) {
        Handler handler = super.onRouteRequest(requestHeader);
        if(basicAuthEnabled) {
            handler = authenticator.requireBasicAuthentication(requestHeader, handler);
        }
        return handler;
    }

    @Override
    public Action onRequest(Http.Request request, Method method) {
        logRequest(request, method);
        return super.onRequest(request, method);
    }

    private void logRequest(Http.Request request, Method method) {
        if(Logger.isDebugEnabled() && !request.path().startsWith("/assets")) {
            StringBuilder sb = new StringBuilder(request.toString());
            sb.append(" ").append(method.getDeclaringClass().getCanonicalName());
            sb.append(".").append(method.getName()).append("(");
            Class<?>[] params = method.getParameterTypes();
            for (int j = 0; j < params.length; j++) {
                sb.append(params[j].getCanonicalName().replace("java.lang.", ""));
                if (j < (params.length - 1))
                    sb.append(',');
            }
            sb.append(")");
            Logger.debug(sb.toString());
        }
    }

    private void initializeSpring() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        configuration.SpringConfiguration.initializeContext(context);
    }

    private void initializeBasicAuthPlugin() {
        basicAuthEnabled = isTrue(Play.application().configuration().getBoolean("basic.auth.enabled"));//use -Dbasic.auth.enabled=true
        Logger.info(basicAuthEnabled ? "basic auth is enabled" : "basic auth is disabled");
    }
}
