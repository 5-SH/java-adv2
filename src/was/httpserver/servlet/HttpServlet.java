package was.httpserver.servlet;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

import java.io.IOException;

public interface HttpServlet {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
