/** Testes do Protocol sem dependencias externas: java -ea -cp out ProtocolTest */
public class ProtocolTest {

    private static int failures = 0;

    public static void main(String[] args) {
        check("ECHO ola mundo", "OK ola mundo", false);
        check("echo minusculo", "OK minusculo", false);
        check("  ECHO   com espacos  ", "OK com espacos", false);
        check("ECHO", "ERR ECHO precisa de uma mensagem", false);
        check("QUIT", "BYE", true);
        check("quit", "BYE", true);
        check("", "ERR comando vazio", false);
        check("HELP", "ERR comando desconhecido: HELP", false);

        if (failures > 0) {
            System.out.println(failures + " teste(s) falharam");
            System.exit(1);
        }
        System.out.println("Todos os testes passaram");
    }

    private static void check(String input, String expectedText, boolean expectedClose) {
        Protocol.Reply r = Protocol.handle(input);
        boolean ok = r.text().equals(expectedText) && r.closeConnection() == expectedClose;
        System.out.println((ok ? "PASS " : "FAIL ") + "'" + input + "' -> '" + r.text() + "'");
        if (!ok) {
            failures++;
        }
    }
}
