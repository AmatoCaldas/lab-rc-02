import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Uso: java EchoClient <nome> <host> [porta]
 *
 * O usuario digita comandos do protocolo (echo <msg> / quit). No ECHO o
 * cliente prefixa a mensagem com o nome, como no exemplo dos slides.
 */
public class EchoClient {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Uso: java EchoClient <nome> <host> [porta]");
            return;
        }
        String screenName = args[0];
        String host = args[1];
        int port = args.length > 2 ? Integer.parseInt(args[2]) : 4444;

        try (Socket socket = new Socket(host, port);
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.err.println("Connected to " + host + " on port " + port);
            System.out.println(in.readLine()); // HELLO

            String s;
            while ((s = stdin.readLine()) != null) {
                out.println(format(screenName, s));
                String reply = in.readLine();
                if (reply == null) {
                    break;
                }
                System.out.println(reply);
                if (Protocol.BYE.equals(reply)) {
                    break;
                }
            }
            // Ctrl-D / Ctrl-Z sem QUIT: avisa o servidor antes de sair
            if (s == null) {
                out.println("QUIT");
            }
            System.err.println("Closing connection to " + host);
        }
    }

    private static String format(String screenName, String line) {
        String trimmed = line.strip();
        if (trimmed.regionMatches(true, 0, "ECHO ", 0, 5)) {
            return "ECHO [" + screenName + "]: " + trimmed.substring(5).strip();
        }
        return trimmed;
    }
}
