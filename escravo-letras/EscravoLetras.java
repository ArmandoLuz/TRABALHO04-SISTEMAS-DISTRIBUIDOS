import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;

public class EscravoLetras {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/letras", new LetrasHandler());
        server.setExecutor(null); // usa executor padrão
        System.out.println("Escravo Letras pronto e escutando em http://localhost:8081/letras");
        server.start();
    }

    static class LetrasHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                System.out.println("Requisição recebida!");
                // Lê o conteúdo do corpo da requisição
                InputStream is = exchange.getRequestBody();
                String conteudo = new String(is.readAllBytes(), "UTF-8");

                // Conta letras
                int count = (int) conteudo.chars().filter(Character::isLetter).count();
                String resposta = String.valueOf(count);

                System.out.println("Quantidade de letras: " + resposta);

                // Envia resposta
                exchange.sendResponseHeaders(200, resposta.length());
                OutputStream os = exchange.getResponseBody();
                os.write(resposta.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Método não permitido
            }
        }
    }
}
