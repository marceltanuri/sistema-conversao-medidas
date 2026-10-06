# Sistema de conversão de medidas

**O mesmo sistema, escrito quatro vezes, para mostrar como as aplicações evoluíram ao longo do tempo:**
do terminal às janelas, das janelas ao navegador e, por fim, ao framework.

Este repositório é um exemplo didático. O problema é simples de propósito (converter metros em pés,
Celsius em Fahrenheit, quilos em libras...), para que a atenção fique no que realmente muda de uma
geração para a outra: **a forma como o usuário conversa com o sistema**.

```
  CLI  ──────►  GUI  ──────►  WEB  ──────►  WEB + FRAMEWORK
terminal      janelas       navegador      navegador + Quarkus
(Scanner)     (Swing)       (HttpServer)   (Maven, CDI, REST)
```

## As quatro versões

| # | Projeto | Interface | Dependências externas | Como roda |
|---|---------|-----------|-----------------------|-----------|
| 1 | [sistema-conversao-medidas-cli](sistema-conversao-medidas-cli) | Menu de texto no terminal (`Scanner`) | Nenhuma | `javac` + `java` |
| 2 | [sistema-conversao-medidas-gui](sistema-conversao-medidas-gui) | Janela desktop (Java Swing) | Nenhuma | `javac` + `java` |
| 3 | [sistema-conversao-medidas-web](sistema-conversao-medidas-web) | Página HTML + API REST | Nenhuma | `javac` + `java`, abrir no navegador |
| 4 | [sistema-conversao-medidas-quarkus](sistema-conversao-medidas-quarkus) | Mesma página + API REST com framework | Quarkus, via Maven | `./mvnw quarkus:dev` |

Cada pasta tem o próprio README, com a estrutura, as instruções para compilar e rodar e exercícios sugeridos.

### 1. CLI: o terminal

A forma mais antiga e mais direta. O programa imprime um menu, lê o que o usuário digita e responde.
Tudo acontece em sequência, um passo depois do outro, e o programa é quem conduz a conversa.

### 2. GUI: as janelas

A interface passa a ser **orientada a eventos**: em vez de o programa perguntar, ele espera o usuário
clicar, escolher em uma lista ou digitar. O resultado é recalculado a cada tecla. A aplicação continua
instalada e rodando na máquina de quem usa.

### 3. WEB: o navegador, sem framework

O sistema se divide em dois: um **servidor** Java, que expõe uma API JSON, e um **cliente** (HTML, CSS e
JavaScript puro) que roda no navegador e chama essa API com `fetch()`. Ninguém precisa instalar nada além
do navegador.

Tudo foi feito "na mão", só com o que já vem no JDK: o servidor é o `com.sun.net.httpserver.HttpServer`,
as rotas são um `switch` no caminho da URL e o JSON é montado com `StringBuilder`. É trabalhoso, e é
justamente esse o ponto: mostrar o que um framework faz por nós.

### 4. WEB + framework: Quarkus

A mesma página e a mesma API, agora com [Quarkus](https://quarkus.io/) e Maven. O trabalho braçal da versão
anterior desaparece: o servidor sobe sozinho, as rotas viram anotações (`@Path`, `@GET`, `@QueryParam`),
o JSON é gerado automaticamente a partir de `record`s, os erros são tratados por um `ExceptionMapper` e o
`ConversorService` é injetado em vez de criado com `new`. Os testes passam a usar JUnit 5 e RestAssured.

O [README da versão Quarkus](sistema-conversao-medidas-quarkus) traz uma tabela comparando, item a item,
o que foi substituído pelo framework.

## O que não mudou: o domínio

Esta é a lição principal do repositório. Em todas as quatro versões, as classes que representam o
**problema** são exatamente as mesmas:

```
modelo/Categoria.java                    # categorias de medida (comprimento, massa, temperatura...)
modelo/Unidade.java                      # unidades e a fórmula de conversão para a unidade base
servico/ConversorService.java            # a regra de conversão
excecao/ConversaoInvalidaException.java  # o erro de negócio
```

Os arquivos são idênticos nas versões CLI, GUI e web. Na versão Quarkus, a única diferença é a anotação
`@ApplicationScoped` no `ConversorService`, para que o framework gerencie a instância. A regra em si
não mudou uma linha.

Isso é possível porque o código foi organizado com **orientação a objetos**, separando responsabilidades:

- **O domínio** (`modelo`, `servico`, `excecao`) sabe converter medidas, e só isso. Não sabe se quem o
  chama é um terminal, uma janela ou uma requisição HTTP.
- **A interface** (`cli`, `gui`, `web`, `rest`) sabe conversar com o usuário, e delega a conversão ao
  domínio.

Em cada evolução, só a camada de interface foi trocada. Tecnologias de interface mudam rápido; as
regras do negócio costumam durar muito mais. Quando elas estão separadas, dá para acompanhar a primeira
sem reescrever a segunda.

### Por que o código do domínio está copiado, e não reaproveitado?

Na vida real, o domínio seria extraído para uma biblioteca (um `.jar` ou um módulo Maven) e as quatro
interfaces dependeriam dela. Aqui optei por **copiar** os mesmos arquivos em cada projeto, de propósito:

- cada pasta é independente e pode ser aberta, compilada e estudada sozinha;
- as três primeiras versões continuam sem gerenciador de dependências, rodando só com `javac` e `java`;
- não é preciso entender empacotamento, *classpath* de bibliotecas ou projetos multimódulo para seguir
  a evolução.

O reaproveitamento continua demonstrado: o mesmo código funcionou, sem alterações, em quatro interfaces
completamente diferentes. Transformá-lo em uma biblioteca compartilhada fica como próximo passo natural
(veja os exercícios abaixo).

## Como funciona a conversão

Cada categoria tem uma **unidade base** (metro, quilograma, litro, metro quadrado, Celsius, segundo e
metro por segundo). Toda conversão acontece em dois passos, `origem -> base -> destino`, usando a fórmula:

```
valorNaBase = valor * fator + deslocamento
```

O `deslocamento` só é diferente de zero nas temperaturas (Fahrenheit e Kelvin). Assim não é preciso
cadastrar uma fórmula para cada par de unidades: basta cada unidade saber ir e voltar da sua base.

Categorias suportadas: Comprimento, Massa, Volume, Área, Temperatura, Tempo e Velocidade.

## Requisitos

- **Versões CLI, GUI e web:** JDK 17 ou superior. Não precisa de Maven, Gradle ou IDE.
- **Versão Quarkus:** JDK 25. O Maven vem embutido no projeto pelo Maven Wrapper (`./mvnw`).

## Sugestão de estudo

1. Rode as quatro versões e faça a mesma conversão em cada uma.
2. Compare os pacotes `modelo` e `servico` entre as pastas e confirme que são iguais.
3. Compare as camadas de interface: `cli/MenuCli.java`, `gui/JanelaConversor.java`, o pacote `web`
   e o pacote `rest`.
4. Leia a tabela "O que o Quarkus substituiu" no README da versão Quarkus.

## Exercícios para quem quiser ir além

1. Adicionar a categoria **Armazenamento de dados** (byte, KB, MB, GB, TB) no domínio e ver que as
   interfaces passam a oferecê-la sem nenhuma alteração.
2. Extrair `modelo`, `servico` e `excecao` para uma biblioteca (`.jar`) e fazer as quatro versões
   dependerem dela, eliminando a cópia.
3. Criar uma quinta versão: um app mobile, um bot de chat ou uma interface de linha de comando com
   argumentos, reaproveitando o mesmo domínio.
