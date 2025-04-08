import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;

public class EscravoNumeros {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/numeros", new NumerosHandler());
        server.setExecutor(null); // executor padrão
        System.out.println("Escravo Números pronto e escutando em http://localhost:8082/numeros");
        server.start();
    }

    static class NumerosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                System.out.println("Requisição recebida!");
                // Lê o corpo da requisição
                InputStream is = exchange.getRequestBody();
                String conteudo = new String(is.readAllBytes(), "UTF-8");

                // Conta dígitos numéricos
                int count = (int) conteudo.chars().filter(Character::isDigit).count();
                String resposta = String.valueOf(count);

                System.out.println("Quantidade de numeros: " + resposta);

                // Envia a resposta
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
