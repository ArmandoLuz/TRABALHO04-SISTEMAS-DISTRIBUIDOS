
# 🧠 Trabalho 04 - Sistemas Distribuídos

Este projeto implementa um sistema distribuído com arquitetura **Mestre-Escravo**, utilizando **Java puro**, **HTTP REST**, **multithreading**, e **Docker**. A interface gráfica foi desenvolvida com **Swing**, permitindo ao usuário enviar um arquivo `.txt` que será processado por dois escravos:

- Um que conta **letras**
- Outro que conta **números**

O **mestre** é responsável por distribuir as tarefas e consolidar os resultados.

---

## 🗂 Estrutura de Diretórios

```
.
├── cliente        
│   └── ClienteGUI.java             # Interface gráfica do cliente
├── docker-compose.yml              # Orquestração dos containers Docker
├── Dockerfile                      # Dockerfile genérico para os serviços
├── escravo-letras
│   └── EscravoLetras.java          # Serviço que conta letras
├── escravo-numeros
│   └── EscravoNumeros.java         # Serviço que conta números
├── mestre
│   └── Mestre.java                 # Mestre que distribui tarefas
└── README.md
```

---

## 🚀 Como Executar

### Pré-requisitos:
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### Passos:

1. Clone o repositório:
```bash
git clone https://github.com/ArmandoLuz/TRABALHO04-SISTEMAS-DISTRIBUIDOS.git
cd TRABALHO04-SISTEMAS-DISTRIBUIDOS
```

2. Suba os containers (Os arquivos `.java` serão compilados nesse processso):
```bash
docker compose up --build
```

3. Habilitar acesso ao X11 (DIspositivos Linux).
```bash 
xhost +local:docker
```

4. Será exibida uma interface gráfica para a seleção e envio do arquivo `.txt`.
---

## 🧪 Funcionamento

1. O **cliente GUI** permite que o usuário selecione um arquivo `.txt`.
2. O conteúdo é enviado via `POST` para o **Mestre**.
3. O **Mestre** cria **duas threads** paralelas:
   - Uma chama o **Escravo Letras** via HTTP.
   - Outra chama o **Escravo Números** via HTTP.
4. Os escravos processam e retornam os resultados.
5. O **Mestre** responde com a contagem consolidada de letras e números.

---

## 🐳 Sobre os Containers

- `cliente-gui`: Interface gráfica Swing.
- `mestre`: Serviço HTTP REST que coordena os escravos.
- `escravo-letras`: Conta o número de letras em um texto.
- `escravo-numeros`: Conta o número de dígitos numéricos.

---

## 📌 Observações

- O projeto utiliza `Executors` para paralelismo no mestre.
- Comunicação entre containers via nomes de host definidos no `docker-compose.yml`.
- Evite arquivos muito grandes, pois a resposta HTTP do mestre possui tamanho fixo (usa `FixedLengthOutputStream`).

---