package was.v8;

import was.httpserver.HttpServer;
import was.httpserver.servlet.ServletManager;
import was.httpserver.servlet.annotation.AnnotationServletV2;
import was.httpserver.servlet.annotation.AnnotationServletV3;
import was.v5.servlet.DiscardServlet;

import java.io.IOException;
import java.util.List;

public class ServerMainV8 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SiteControllerV8(), new SearchControllerV8());
        ServletManager servletManager = new ServletManager();
//        servletManager.setDefaultServlet(new AnnotationServletV2(controllers));
        servletManager.setDefaultServlet(new AnnotationServletV3(controllers));
        servletManager.add("/favicon.ico", new DiscardServlet());
        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
