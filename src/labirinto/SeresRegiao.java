package labirinto;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class SeresRegiao {
    private Map<Character, Integer> contagem;
    private PriorityQueue<Map.Entry<Character, Integer>> filaFrequencias;

    public SeresRegiao() {
        this.contagem = new HashMap<>();
        filaFrequencias = new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
    }

    public void adicionarSer(char ser) {
        contagem.put(ser, contagem.getOrDefault(ser, 0) + 1);
        filaFrequencias.add(new AbstractMap.SimpleEntry<>(ser, contagem.get(ser)));

    }

    public char serMaisFrequente() {
        return contagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(' ');
    }

    public Map<Character, Integer> getContagem() {
        return contagem;
    }
}