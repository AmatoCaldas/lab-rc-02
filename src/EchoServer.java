import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 4444;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Started server on port " + port);

        while (true) {
            // espera blocante ate alguma requisicao de conexao
            Socket clientSocket = serverSocket.accept();
            System.err.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());

            // cada cliente e atendido em uma thread propria; o loop volta
            // imediatamente para o accept() e aceita novas conexoes
            Thread t = new Thread(new ClientHandler(clientSocket));
            t.start();
        }
    }
}
