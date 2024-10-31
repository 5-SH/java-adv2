package was.v7;

import was.httpserver.HttpServer;
import was.httpserver.servlet.ServletManager;
import was.httpserver.servlet.annotation.AnnotationServletV1;
import was.v5.servlet.DiscardServlet;

import java.io.IOException;
import java.util.List;

public class ServerMainV7 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SiteControllerV7(), new SearchControllerV7());
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(new AnnotationServletV1(controllers));
        servletManager.add("/favicon.ico", new DiscardServlet());
        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
