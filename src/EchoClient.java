import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) throws Exception {
        String screenName = args[0];
        String host = args[1];
        int port = 4444;

        // conecta ao servidor e abre os streams
        Socket socket = new Socket(host, port);
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        System.err.println("Connected to " + host + " on port " + port);

        // le da entrada padrao, envia, escreve resposta
        String s;
        while ((s = stdin.readLine()) != null) {
            out.println("[" + screenName + "]: " + s);
            System.out.println(in.readLine());
        }

        // encerra os sockets
        System.err.println("Closing connection to " + host);
        out.close();
        in.close();
        socket.close();
    }
}
