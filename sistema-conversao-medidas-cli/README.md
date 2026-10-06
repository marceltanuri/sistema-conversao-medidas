# Sistema de conversao de medidas CLI

É um sistema java, sem dependencias externas, sem gerenciador de dependencia, para fins didaticos, que converte valores de unidades de medida em geral.

## Estrutura

```
src/br/com/ada/conversor/
├── Main.java                              # ponto de entrada
├── cli/MenuCli.java                       # interação com o usuário (Scanner)
├── modelo/Categoria.java                  # enum das categorias de medida
├── modelo/Unidade.java                    # enum das unidades + fórmula de conversão
├── servico/ConversorService.java          # regra de conversão
└── excecao/ConversaoInvalidaException.java
test/br/com/ada/conversor/ConversorServiceTest.java  # testes sem JUnit
```

## Como funciona a conversão

Cada categoria tem uma **unidade base** (metro, quilograma, litro, m², Celsius, segundo, m/s).
Toda conversão acontece em dois passos: `origem -> base -> destino`, usando:

```
valorNaBase = valor * fator + deslocamento
```

O `deslocamento` só é diferente de zero nas temperaturas (Fahrenheit e Kelvin).

Categorias suportadas: Comprimento, Massa, Volume, Área, Temperatura, Tempo e Velocidade.

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

1. Adicionar a categoria **Armazenamento de dados** (byte, KB, MB, GB, TB).
2. Permitir conversão direta via argumentos: `java -cp out br.com.ada.conversor.Main 10 km mi`.
3. Exibir um histórico das conversões feitas na sessão.
