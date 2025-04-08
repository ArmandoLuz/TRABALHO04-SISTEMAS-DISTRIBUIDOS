import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Mestre {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/processar", new MestreHandler());
        server.setExecutor(Executors.newFixedThreadPool(4)); // Pool para múltiplos pedidos
        System.out.println("Mestre aguardando requisições...");
        server.start();
    }

    static class MestreHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Lê o corpo da requisição
                InputStream input = exchange.getRequestBody();
                String conteudo = new String(input.readAllBytes(), "UTF-8");
                System.out.println("Requisição recebida pelo mestre.");

                try {
                    ExecutorService pool = Executors.newFixedThreadPool(2);

                    Future<Integer> letras = pool.submit(() -> chamaEscravo("http://escravo-letras:8081/letras", conteudo));
                    System.out.println("Requisição para contagem de letras enviada para o escravo de letras");
                    Future<Integer> numeros = pool.submit(() -> chamaEscravo("http://escravo-numeros:8082/numeros", conteudo));
                    System.out.println("Requisição para contagem de numeros enviada para o escravo de números");

                    int resultadoLetras = letras.get();
                    int resultadoNumeros = numeros.get();

                    String respostaFinal = "Letras: " + resultadoLetras + " | Números: " + resultadoNumeros + "\n";
                    byte[] respostaBytes = respostaFinal.getBytes("UTF-8");

                    exchange.sendResponseHeaders(200, respostaBytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(respostaBytes);
                    os.close();

                    pool.shutdown();

                } catch (Exception e) {
                    e.printStackTrace();
                    String erro = "Erro ao processar: " + e.getMessage();
                    exchange.sendResponseHeaders(500, erro.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(erro.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        }

        private int chamaEscravo(String urlStr, String conteudo) throws Exception {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(conteudo.getBytes("UTF-8"));
                os.flush();
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                return Integer.parseInt(in.readLine());
            }
        }
    }

}
