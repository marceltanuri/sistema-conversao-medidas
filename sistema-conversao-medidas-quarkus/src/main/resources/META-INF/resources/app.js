// Toda a regra de conversão fica no servidor Java.
// Este script só monta a tela e chama a API com fetch().

const campoCategoria = document.getElementById("categoria");
const campoOrigem = document.getElementById("origem");
const campoDestino = document.getElementById("destino");
const campoValor = document.getElementById("valor");
const botaoInverter = document.getElementById("inverter");
const textoResultado = document.getElementById("resultado");

let categorias = [];

async function iniciar() {
    const resposta = await fetch("/api/unidades");
    categorias = await resposta.json();

    for (const categoria of categorias) {
        campoCategoria.add(new Option(categoria.descricao, categoria.id));
    }

    campoCategoria.addEventListener("change", atualizarUnidades);
    campoOrigem.addEventListener("change", converter);
    campoDestino.addEventListener("change", converter);
    campoValor.addEventListener("input", converter);
    botaoInverter.addEventListener("click", inverterUnidades);

    atualizarUnidades();
}

function atualizarUnidades() {
    const categoria = categorias.find(c => c.id === campoCategoria.value);

    campoOrigem.innerHTML = "";
    campoDestino.innerHTML = "";
    for (const unidade of categoria.unidades) {
        const texto = `${unidade.nome} (${unidade.simbolo})`;
        campoOrigem.add(new Option(texto, unidade.id));
        campoDestino.add(new Option(texto, unidade.id));
    }

    // já sugere um par diferente, para a conversão não começar "1 = 1"
    if (campoDestino.options.length > 1) {
        campoDestino.selectedIndex = 1;
    }
    converter();
}

function inverterUnidades() {
    const origem = campoOrigem.value;
    campoOrigem.value = campoDestino.value;
    campoDestino.value = origem;
    converter();
}

async function converter() {
    if (campoValor.value.trim() === "") {
        mostrarResultado("", false);
        return;
    }

    const parametros = new URLSearchParams({
        valor: campoValor.value,
        origem: campoOrigem.value,
        destino: campoDestino.value,
    });

    const resposta = await fetch(`/api/converter?${parametros}`);
    const dados = await resposta.json();

    if (!resposta.ok) {
        mostrarResultado(dados.erro, true);
        return;
    }
    mostrarResultado(
        `${formatar(dados.valor)} ${dados.origem} = ${formatar(dados.resultado)} ${dados.destino}`,
        false
    );
}

function mostrarResultado(texto, ehErro) {
    textoResultado.textContent = texto;
    textoResultado.classList.toggle("erro", ehErro);
}

function formatar(numero) {
    // até 6 casas decimais, sem zeros desnecessários: 2.500000 -> 2.5
    return Number(numero.toFixed(6)).toString();
}

iniciar();
