package was.v5.servlet;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.HttpServlet;

import java.io.IOException;

public class Site2Servlet implements HttpServlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.writeBody("<h1>site2</h1>");
    }
}
