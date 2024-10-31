package webservice;

import io.member.FileMemberRepository;
import io.member.MemberRepository;
import io.member.MemoryMemberRepository;
import was.httpserver.HttpServer;
import was.httpserver.servlet.HttpServlet;
import was.httpserver.servlet.ServletManager;
import was.httpserver.servlet.annotation.AnnotationServletV3;
import was.v5.servlet.DiscardServlet;

import java.io.IOException;
import java.util.List;

public class MemberServerMain {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        MemberRepository memberRepository = new FileMemberRepository();
        MemberController memberController = new MemberController(memberRepository);
        HttpServlet servlet = new AnnotationServletV3(List.of(memberController));

        ServletManager servletManager = new ServletManager();
        servletManager.add("/favicon.ico", new DiscardServlet());
        servletManager.setDefaultServlet(servlet);

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
