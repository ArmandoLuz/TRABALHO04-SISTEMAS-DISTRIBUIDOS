// ========== ClienteGUI.java ========== //
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cliente");
        JButton button = new JButton("Enviar arquivo para Mestre");

        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("/host/ufpi");
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    URL url = new URL("http://mestre:8000/processar");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "text/plain");

                    // Envia o conteúdo do arquivo
                    try (OutputStream os = conn.getOutputStream();
                        FileInputStream fis = new FileInputStream(file)) {
                        fis.transferTo(os);
                        os.flush();
                        os.close(); 
                    }

                    // Força leitura da resposta
                    int responseCode = conn.getResponseCode();
                    System.out.println("Código de resposta do Mestre: " + responseCode);

                    // Lê o conteúdo textual da resposta
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String linha;
                    StringBuilder resposta = new StringBuilder();
                    while ((linha = in.readLine()) != null) {
                        resposta.append(linha);
                    }
                    in.close();

                    System.out.println("Resposta do Mestre: " + resposta.toString());
                    JOptionPane.showMessageDialog(null, "Resposta do Mestre:\n" + resposta.toString());

                    conn.disconnect();
                } catch (Exception error) {
                    error.printStackTrace();
                }

            }
        });

        frame.add(button);
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}