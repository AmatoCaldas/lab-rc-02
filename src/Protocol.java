/**
 * Protocolo texto, uma mensagem por linha.
 *
 * Cliente -> Servidor:
 *   ECHO <mensagem>   pede que o servidor devolva a mensagem
 *   QUIT              encerra a conexao
 *
 * Servidor -> Cliente:
 *   HELLO <texto>     saudacao enviada ao conectar
 *   OK <mensagem>     resposta ao ECHO
 *   BYE               resposta ao QUIT (servidor fecha a conexao em seguida)
 *   ERR <motivo>      comando desconhecido ou mal formado
 *
 * Comandos nao diferenciam maiusculas de minusculas (echo == ECHO).
 */
public final class Protocol {

    public static final String GREETING = "HELLO EchoServer pronto. Comandos: ECHO <msg> | QUIT";
    public static final String BYE = "BYE";

    private Protocol() {
    }

    /** Resultado do processamento de uma linha: a resposta e se a conexao deve ser encerrada. */
    public record Reply(String text, boolean closeConnection) {
    }

    public static Reply handle(String line) {
        String trimmed = line.strip();
        if (trimmed.isEmpty()) {
            return new Reply("ERR comando vazio", false);
        }

        int space = trimmed.indexOf(' ');
        String command = (space < 0 ? trimmed : trimmed.substring(0, space)).toUpperCase();
        String param = space < 0 ? "" : trimmed.substring(space + 1).strip();

        switch (command) {
            case "ECHO":
                if (param.isEmpty()) {
                    return new Reply("ERR ECHO precisa de uma mensagem", false);
                }
                return new Reply("OK " + param, false);
            case "QUIT":
                return new Reply(BYE, true);
            default:
                return new Reply("ERR comando desconhecido: " + command, false);
        }
    }
}
