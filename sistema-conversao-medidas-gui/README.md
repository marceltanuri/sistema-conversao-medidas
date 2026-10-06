# Sistema de conversao de medidas GUI

Versão em janelas (Java Swing) do [sistema-conversao-medidas-cli](../sistema-conversao-medidas-cli).
Continua sem dependências externas e sem gerenciador de dependências, para fins didáticos.

## Estrutura

```
src/br/com/ada/conversor/
├── Main.java                              # ponto de entrada (abre a janela na EDT)
├── gui/JanelaConversor.java               # tela Swing
├── modelo/Categoria.java                  # enum das categorias de medida
├── modelo/Unidade.java                    # enum das unidades + fórmula de conversão
├── servico/ConversorService.java          # regra de conversão
└── excecao/ConversaoInvalidaException.java
test/br/com/ada/conversor/ConversorServiceTest.java  # testes sem JUnit
```

Os pacotes `modelo`, `servico` e `excecao` são **idênticos** aos da versão CLI.
Só a camada de interface mudou: `cli/MenuCli.java` virou `gui/JanelaConversor.java`.
Esse é o ganho de separar a regra de negócio da interface com o usuário.

## Como usar

1. Escolha a categoria (Comprimento, Massa, Volume, Área, Temperatura, Tempo ou Velocidade).
2. Escolha as unidades de origem e destino.
3. Digite o valor: o resultado é atualizado a cada tecla.
4. O botão **Inverter unidades** troca origem e destino.

## Como compilar e executar

```bash
javac -d out $(find src -name "*.java")
java -cp out br.com.ada.conversor.Main
```

## Como rodar os testes

```bash
javac -d out $(find src test -name "*.java")
java -cp out br.com.ada.conversor.ConversorServiceTest
```

## Exercícios sugeridos

1. Adicionar a categoria **Armazenamento de dados** (byte, KB, MB, GB, TB) e ver que a tela já funciona sem mudanças.
2. Adicionar uma `JList` com o histórico das conversões feitas.
3. Adicionar um botão para copiar o resultado para a área de transferência.
