import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            System.err.println("Accepted connection from client");

            // cria as streams para o socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // espera a leitura do dado (ate terminar conexao)
            String s;
            while ((s = in.readLine()) != null) {
                out.println(s);
            }

            // fecha a conexao (e o socket)
            System.err.println("Closing connection with client");
            out.close();
            in.close();
            clientSocket.close();
        }
    }
}
