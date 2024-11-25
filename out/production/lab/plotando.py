import matplotlib.pyplot as plt

# Dados fornecidos
casos = [40, 80, 100, 120, 150, 180, 200, 250]
tempo_medio = [4.25426064, 6.19372830, 2.41920428, 5.96359935, 5.69426179, 3.71361184, 5.12571349, 6.15876770]

# Plotando o gráfico
plt.plot(casos, tempo_medio, marker='o', color='b', linestyle='-', markersize=6)

# Adicionando título e rótulos aos eixos
plt.title("Tempo Médio de Execução por Caso")
plt.xlabel("Caso")
plt.ylabel("Tempo Médio de Execução (s)")

# Usando escala logarítmica no eixo Y
plt.yscale('log')
plt.xscale('log')

# Exibindo o gráfico
plt.grid(True)
plt.show()
