# 🏛️ O Labirinto do Horror II 🏛️ 

## 📜 Descrição do Problema
Durante escavações arqueológicas em Creta, foi descoberta uma antiga civilização que codificava labirintos em um sistema de células com numeração hexadecimal. Esses labirintos, descritos por uma matriz de células **m x n**, foram criados com um sistema de paredes nas direções: 
- Superior
- Direita
- Inferior
- Esquerda

Cada direção é representada por um número de **4 bits** em formato binário.

O problema dos antigos cretenses era que esses labirintos muitas vezes tinham regiões inatingíveis, o que poderia isolar diferentes seres e personagens (como Teseu, Ariadne e o Minotauro) em áreas distintas. Além disso, cada célula do labirinto poderia conter seres místicos, como Anões, Bruxas, Cavaleiros, Duendes, Elfos e Feijões, indicados em **hexadecimal e letras maiúsculas**.

## 🎯 Objetivo
A missão deste projeto é processar arquivos de diferentes labirintos e determinar:
1. **Quantas regiões isoladas** existem no labirinto.
2. **Qual ser aparece com mais frequência em cada região isolada**.

## 🔍 Solução
Para resolver o problema, utilizamos técnicas de grafos e estruturas de dados para identificar componentes conexos dentro do labirinto, aplicando o algoritmo **Union-Find** para identificar regiões isoladas.

A solução considera:
- Representação do labirinto como uma matriz **m x n**.
- Leitura e decodificação das paredes de cada célula em números binários.
- Contagem dos seres em cada região isolada.

### 🧩 Algoritmo
1. **Decodificação do labirinto.Labirinto**: Cada célula é lida e convertida para uma estrutura que armazena suas paredes em formato binário.
2. **Exploração das Regiões**: Utilizando busca em largura (BFS) ou profundidade (DFS), percorremos cada célula para identificar regiões isoladas.
3. **Contagem de Seres**: Contamos a frequência de cada tipo de ser em cada região e identificamos o ser dominante.
4. **Resultados**: Relatamos o número total de regiões isoladas e o ser mais frequente em cada uma delas.

👥 **Autores**: Maria Rita e Murilo dos Santos
