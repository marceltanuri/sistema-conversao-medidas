# Sistema de conversao de medidas WEB

Versão web do [sistema-conversao-medidas-cli](../sistema-conversao-medidas-cli) e do
[sistema-conversao-medidas-gui](../sistema-conversao-medidas-gui).
Continua sem frameworks, sem dependências externas e sem gerenciador de dependências, para fins didáticos:

- **Back-end:** `com.sun.net.httpserver.HttpServer`, o servidor HTTP que já vem no JDK (sem Spring, sem Tomcat).
- **Front-end:** HTML, CSS e JavaScript puro (sem React, sem jQuery).
- **JSON:** montado à mão com `StringBuilder` (sem Jackson, sem Gson).

## Estrutura

```
src/br/com/ada/conversor/
├── Main.java                              # sobe o servidor na porta 8881
├── web/ApiHandler.java                    # endpoints /api/*
├── web/ArquivoEstaticoHandler.java        # entrega os arquivos da pasta public
├── web/Json.java                          # monta as respostas JSON
├── modelo/Categoria.java                  # enum das categorias de medida
├── modelo/Unidade.java                    # enum das unidades + fórmula de conversão
├── servico/ConversorService.java          # regra de conversão
└── excecao/ConversaoInvalidaException.java
public/
├── index.html                             # a tela
├── style.css
└── app.js                                 # chama a API com fetch()
test/br/com/ada/conversor/ConversorServiceTest.java  # testes sem JUnit
```

Os pacotes `modelo`, `servico` e `excecao` são **idênticos** aos das versões CLI e GUI.
Só a camada de interface mudou, agora dividida em duas partes: o servidor Java (pacote `web`)
e a página que roda no navegador (pasta `public`).

## API

| Método | Caminho | Exemplo de resposta |
|--------|---------|---------------------|
| GET | `/api/unidades` | `[{"id":"COMPRIMENTO","descricao":"Comprimento","unidades":[{"id":"METRO","nome":"Metro","simbolo":"m"}, ...]}, ...]` |
| GET | `/api/converter?valor=98.6&origem=FAHRENHEIT&destino=CELSIUS` | `{"valor":98.6,"origem":"F","resultado":37.0,"destino":"C"}` |

Em caso de erro a API responde com status `400` e `{"erro":"mensagem"}`.

## Como compilar e executar

Execute **a partir desta pasta**, pois o servidor lê os arquivos da pasta `public` pelo caminho relativo:

```bash
javac -d out $(find src -name "*.java")
java -cp out br.com.ada.conversor.Main
```

Depois abra http://localhost:8881 no navegador. Para testar a API direto pelo terminal:

```bash
curl "http://localhost:8881/api/converter?valor=1&origem=QUILOMETRO&destino=MILHA"
```

## Como rodar os testes

```bash
javac -d out $(find src test -name "*.java")
java -cp out br.com.ada.conversor.ConversorServiceTest
```

## Exercícios sugeridos

1. Adicionar a categoria **Armazenamento de dados** (byte, KB, MB, GB, TB) e ver que a página já funciona sem mudanças.
2. Criar um endpoint `GET /api/historico` que devolva as últimas 10 conversões feitas no servidor.
3. Permitir escolher a porta pela linha de comando: `java -cp out br.com.ada.conversor.Main 9090`.
