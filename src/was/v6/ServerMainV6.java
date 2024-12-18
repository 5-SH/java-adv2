package was.v6;

import was.httpserver.HttpServer;
import was.httpserver.servlet.HttpServlet;
import was.httpserver.servlet.ReflectionServlet;
import was.httpserver.servlet.ServletManager;
import was.v5.servlet.DiscardServlet;
import was.v5.servlet.HomeServlet;

import java.io.IOException;
import java.util.List;

public class ServerMainV6 {

    private final static int PORT = 12345;

    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SiteControllerV6(), new SearchControllerV6());
        HttpServlet reflectionServlet = new ReflectionServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(reflectionServlet);
        servletManager.add("/", new HomeServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
