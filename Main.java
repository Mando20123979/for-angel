import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A tiny web server that serves the "I Like You" heart page.
 * No libraries needed, just Java 17 or newer.
 *
 * Run it:   java Main.java
 * Then open http://localhost:8080
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // Hosting services set PORT for you. Locally it falls back to 8080.
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        byte[] page = Files.readAllBytes(Path.of("index.html"));
        byte[] notFound = "Page not found".getBytes(StandardCharsets.UTF_8);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            boolean isHome = path.equals("/") || path.equals("/index.html");

            byte[] body = isHome ? page : notFound;
            String type = isHome ? "text/html; charset=utf-8" : "text/plain; charset=utf-8";

            exchange.getResponseHeaders().set("Content-Type", type);
            exchange.sendResponseHeaders(isHome ? 200 : 404, body.length);
            try (var out = exchange.getResponseBody()) {
                out.write(body);
            }
        });

        server.start();
        System.out.println("Heart page is live at http://localhost:" + port);
    }
}
