import time
import subprocess

def medir_tempo_execucao(comando, n_execucoes=5):

    tempos = []
    for _ in range(n_execucoes):
        inicio = time.time()  # Marca o início
        subprocess.run(comando, shell=True, check=True)  # Executa o comando
        fim = time.time()  # Marca o fim
        tempos.append(fim - inicio)  # Calcula o tempo de execução para essa execução
    
    tempo_medio = sum(tempos) / n_execucoes  # Calcula o tempo médio
    return tempo_medio

# Exemplo de uso
if __name__ == "__main__":
    # Comando para rodar o programa Java diretamente no terminal
    comando = "java Main"  # Substitua por "java -cp caminho/do/classe Main" se necessário
    tempo_medio = medir_tempo_execucao(comando, n_execucoes=5)  # Roda 10 execuções
    print(f"Tempo médio de execução para 10 execuções: {tempo_medio:.8f} segundos")
