// imports

package labirinto;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class SeresRegiao {
    private Map<Character, Integer> contagem;
    private PriorityQueue<Map.Entry<Character, Integer>> filaFrequencias;

    // limite de seres por região
    private final int MAX_SERES = 1000;

    public SeresRegiao() {
        try {
            this.contagem = new HashMap<>();
            this.filaFrequencias = new PriorityQueue<>((a, b) -> {
                if (a == null || b == null)
                    throw new IllegalArgumentException("Comparação inválida: entrada nula");

                // compara por valor (frequência)
                int compareByValue = b.getValue().compareTo(a.getValue());
                if (compareByValue != 0)
                    return compareByValue;

                // em caso de empate, ordena por caractere
                return a.getKey().compareTo(b.getKey());
            });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar SeresRegiao: " + e.getMessage(), e);
        }
    }

    public void adicionarSer(char ser) {
        try {
            if (!Character.isUpperCase(ser))
                throw new IllegalArgumentException("Ser inválido: deve ser uma letra maiúscula");

            // se excedeu o limite de seres
            if (contagem.size() >= MAX_SERES)
                throw new IllegalStateException("Limite máximo de seres atingido na região");

            // atualiza a contagem
            int novaContagem = contagem.getOrDefault(ser, 0) + 1;

            // verifica overflow
            if (novaContagem <= 0)
                throw new IllegalStateException("Overflow na contagem de seres");

            contagem.put(ser, novaContagem);

            // atualiza a fila de prioridade
            try {
                // remove entrada anterior (se existir)
                filaFrequencias.removeIf(entry -> entry.getKey().equals(ser));
                filaFrequencias.add(new AbstractMap.SimpleEntry<>(ser, novaContagem)); //  nova entrada
            } catch (Exception e) {
                System.err.println("Erro ao atualizar fila de frequências: " + e.getMessage());
                // continua execução (contagem está correta)
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao adicionar ser: " + e.getMessage(), e);
        }
    }

    public char serMaisFrequente() {
        try {
            // se há seres registrados
            if (contagem == null || contagem.isEmpty())
                // retorna espaço em branco se não houver seres
                return ' ';

            return contagem.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> {
                        // valida o resultado
                        if (!Character.isUpperCase(entry.getKey()))
                            throw new IllegalStateException("Ser inválido encontrado na contagem");

                        return entry.getKey();
                    })
                    .orElse(' ');

        } catch (IllegalStateException e) {
            System.err.println("Erro ao obter ser mais frequente: " + e.getMessage());
            return ' ';
        } catch (Exception e) {
            System.err.println("Erro inesperado ao obter ser mais frequente: " + e.getMessage());
            return ' ';
        }
    }

    public Map<Character, Integer> getContagem() {
        try {
            // retorna uma cópia do mapa
            return new HashMap<>(contagem);
        } catch (Exception e) {
            System.err.println("Erro ao obter contagem: " + e.getMessage());
            return new HashMap<>();
        }
    }
}