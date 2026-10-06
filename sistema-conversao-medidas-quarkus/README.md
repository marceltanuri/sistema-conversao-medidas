# Sistema de conversao de medidas QUARKUS

Versão com framework do sistema de conversão de medidas, agora **com gerenciador de dependências (Maven)**.
É a evolução direta do [sistema-conversao-medidas-web](../sistema-conversao-medidas-web): a mesma página
e a mesma API, mas o trabalho "braçal" passa a ser do [Quarkus](https://quarkus.io/) (versão 3.40.1).

## O que o Quarkus substituiu

| Versão web (sem framework)                         | Versão Quarkus                                             |
|----------------------------------------------------|------------------------------------------------------------|
| `HttpServer` criado à mão no `Main`                | Servidor embutido, sem `Main`                              |
| `ApiHandler` com `switch` no caminho da URL        | `@Path`, `@GET` e `@QueryParam` em `ConversorResource`     |
| `Json.java` montando texto com `StringBuilder`     | `record`s (DTOs) convertidos pelo Jackson automaticamente  |
| `try/catch` respondendo erro 400                   | `ConversaoInvalidaExceptionMapper`                         |
| `ArquivoEstaticoHandler` lendo a pasta `public`    | Arquivos em `src/main/resources/META-INF/resources`        |
| `new ConversorService()`                           | `@ApplicationScoped` + injeção pelo construtor             |
| Teste com `main` e `System.out`                    | JUnit 5 + RestAssured                                      |

Os pacotes `modelo` e `excecao` continuam **idênticos** aos das versões CLI, GUI e web.
O `ConversorService` só ganhou a anotação `@ApplicationScoped`.

## Estrutura

```
pom.xml                                    # dependências e plugins do Maven
mvnw / mvnw.cmd                            # Maven Wrapper: roda o Maven sem precisar instalá-lo
src/main/java/br/com/ada/conversor/
├── modelo/Categoria.java                  # enum das categorias de medida
├── modelo/Unidade.java                    # enum das unidades + fórmula de conversão
├── servico/ConversorService.java          # regra de conversão (bean CDI)
├── excecao/ConversaoInvalidaException.java
└── rest/
    ├── ConversorResource.java             # endpoints /api/*
    ├── ConversaoInvalidaExceptionMapper.java
    └── dto/                               # records que viram JSON
src/main/resources/
├── application.properties                 # configurações do Quarkus
└── META-INF/resources/                    # index.html, style.css e app.js (iguais aos da versão web)
src/test/java/br/com/ada/conversor/
├── servico/ConversorServiceTest.java      # teste de unidade (não sobe o Quarkus)
└── rest/ConversorResourceTest.java        # teste de integração (@QuarkusTest)
src/main/docker/                           # Dockerfiles gerados pelo Quarkus
```

## Dependências (pom.xml)

- `quarkus-rest-jackson`: endpoints REST (Jakarta REST) com JSON via Jackson.
- `quarkus-arc`: injeção de dependências (CDI).
- `quarkus-junit` e `rest-assured`: testes.

As versões não aparecem em cada dependência porque vêm do **BOM** `quarkus-bom`, importado em `<dependencyManagement>`.

## API

| Método | Caminho | Exemplo de resposta |
|--------|---------|---------------------|
| GET | `/api/unidades` | `[{"id":"COMPRIMENTO","descricao":"Comprimento","unidades":[{"id":"METRO","nome":"Metro","simbolo":"m"}, ...]}, ...]` |
| GET | `/api/converter?valor=98.6&origem=FAHRENHEIT&destino=CELSIUS` | `{"valor":98.6,"origem":"F","resultado":37.0,"destino":"C"}` |

Em caso de erro a API responde com status `400` e `{"erro":"mensagem"}`.

## Como executar

Modo de desenvolvimento, com *live reload* (altere o código e recarregue o navegador):

```bash
./mvnw quarkus:dev
```

Abra http://localhost:8080. A Dev UI fica em http://localhost:8080/q/dev-ui.

Para empacotar e rodar como em produção:

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

## Como rodar os testes

```bash
./mvnw test
```

## Exercícios sugeridos

1. Adicionar a categoria **Armazenamento de dados** (byte, KB, MB, GB, TB) e ver que a API e a página já funcionam sem mudanças.
2. Adicionar a extensão `quarkus-smallrye-openapi` (`./mvnw quarkus:add-extension -Dextensions=smallrye-openapi`) e explorar a API em `/q/swagger-ui`.
3. Guardar o histórico de conversões em banco com Hibernate ORM com Panache e expor `GET /api/historico`.
4. Mudar a porta para 9090 em `application.properties` (`quarkus.http.port=9090`).
