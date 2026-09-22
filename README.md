# Lab RC 02 — Servidor de Echo multi-threaded com protocolo

Redes de Computadores — DCC/UFJF.

## Tarefa
1. Tornar o servidor de echo **multi-threaded**.
2. Criar um **protocolo** simples com os comandos `echo <msg>` e `quit`.

## Estrutura
| Arquivo | Papel |
|---|---|
| `src/EchoServer.java` | Escuta na porta (padrão 4444) e cria uma thread por cliente |
| `src/ClientHandler.java` | Atende um cliente seguindo o protocolo |
| `src/Protocol.java` | Parse dos comandos e montagem das respostas |
| `src/EchoClient.java` | Cliente de linha de comando |
| `src/ProtocolTest.java` | Testes do protocolo (sem dependências) |

## Protocolo
Texto, uma mensagem por linha. Comandos não diferenciam maiúsculas/minúsculas.

| Direção | Mensagem | Significado |
|---|---|---|
| S → C | `HELLO <texto>` | Saudação ao conectar |
| C → S | `ECHO <msg>` | Pede o eco da mensagem |
| S → C | `OK <msg>` | Eco da mensagem |
| C → S | `QUIT` | Encerra a conexão |
| S → C | `BYE` | Confirma o encerramento; o servidor fecha o socket |
| S → C | `ERR <motivo>` | Comando desconhecido ou `ECHO` sem mensagem |

Máquina de estados: `CONECTADO --HELLO--> PRONTO --ECHO/OK, ERR--> PRONTO --QUIT/BYE--> FECHADO`.

## Como rodar
```bash
javac -d out src/*.java
java -cp out EchoServer 4444
java -cp out EchoClient alex localhost        # em outro terminal (pode abrir vários)
java -cp out ProtocolTest
```

Exemplo de sessão:
```
HELLO EchoServer pronto. Comandos: ECHO <msg> | QUIT
echo this is a test
OK [alex]: this is a test
quit
BYE
```

## Observações
- No código dos slides o laço do cliente usa `while (!stdin.hasNextLine())`, que
  está invertido (sairia na hora com entrada disponível). Aqui o laço lê até
  `readLine()` retornar `null` (Ctrl-D / Ctrl-Z) ou até receber `BYE`.
- As classes `In`/`Out` dos slides foram trocadas por `BufferedReader`/`PrintWriter`
  da biblioteca padrão, para compilar sem dependências.
