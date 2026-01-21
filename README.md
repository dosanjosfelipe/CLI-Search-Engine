# SearchEngine

SearchEngine é uma ferramenta de linha de comando (CLI) desenvolvida para indexação e busca de termos em documentos locais. O sistema utiliza um algoritmo de pontuação para ranquear arquivos com base na relevância e frequência do assunto pesquisado, permitindo uma recuperação de informações eficiente diretamente pelo terminal.

## 🚀 Funcionalidades

- **Varredura Recursiva:** Percorre documentos em diretórios específicos em busca de termos-chave.  
- **Algoritmo de Ranking:** Classifica os documentos encontrados através de um sistema de pontuação baseado na ocorrência dos termos.  
- **Interface CLI:** Execução rápida e direta via terminal através de comandos globais.  

## 🛠️ Pré-requisitos

- Ambiente de execução java configurado.  
- Acesso ao terminal do Windows (CMD ou PowerShell).  

## 🔧 Instalação e Configuração

### Clone o repositório

```bash
git clone https://github.com/dosanjosfelipe/SearchEngine.git
cd SearchEngine
```
## Configuração do Binário (Windows)

Crie uma pasta chamada `searchEngine` dentro da pasta crie outra chamada `bin` na raiz do projeto.

Dentro da pasta `bin`, crie um arquivo chamado `search.bat`.

Insira o seguinte conteúdo no arquivo:
```bash
@echo off
java -jar "%~dp0\searchEngine-1.0-SNAPSHOT.jar" %*
```

### 3. Configuração do PATH

1. Abra o menu Iniciar e digite **"Editar as variáveis de ambiente do sistema"**.  
2. Clique no botão **Variáveis de Ambiente**.  
3. Na seção **Variáveis de Usuário**, localize e selecione a variável `Path`, então clique em **Editar**.  
4. Clique em **Novo** e adicione o caminho completo da pasta `bin` que você criou.  
5. Clique em **OK** em todas as janelas e reinicie o terminal.

## 💻 Como Usar

Com a configuração concluída, execute buscas diretamente pelo terminal:

```bash
search termo desejado
```
O sistema exibirá os documentos encontrados e seus respectivos scores no console.

### Exemplo de uso:
<p align="center">
  <img src="https://github.com/user-attachments/assets/abb6ce86-e9ce-423d-b38d-8514191530e3"
       width="48%" />
  <img src="https://github.com/user-attachments/assets/f1bd799c-2ded-4468-97ec-039936cd7801"
       width="48%" />
</p>


