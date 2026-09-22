import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** Atende um unico cliente em sua propria thread, seguindo o Protocol. */
public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String who = clientSocket.getRemoteSocketAddress().toString();
        try (Socket socket = clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(Protocol.GREETING);

            String line;
            while ((line = in.readLine()) != null) {
                Protocol.Reply reply = Protocol.handle(line);
                out.println(reply.text());
                if (reply.closeConnection()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro com " + who + ": " + e.getMessage());
        }
        System.err.println("Closing connection with " + who);
    }
}
