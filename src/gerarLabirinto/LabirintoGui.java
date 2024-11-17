package gerarLabirinto;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import labirinto.*;

public class LabirintoGui extends JFrame {
    private char[][] labirinto;
    private Map<Integer, Color> coresRegioes;
    private int[][] regioes;

    private final int MIN_MARGIN = 20;
    private final int MIN_CELL_SIZE = 5;
    private final int MAX_CELL_SIZE = 30;
    private final int WALL_THICKNESS = 2;

    private int cellSize;
    private int marginX;
    private int marginY;

    private class LabirintoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            calcularDimensoes(getWidth(), getHeight());
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            // area total do labirinto
            int totalWidth = labirinto[0].length * cellSize;
            int totalHeight = labirinto.length * cellSize;
            g2d.setColor(new Color(250, 250, 250));
            g2d.fillRect(marginX - WALL_THICKNESS,
                    marginY - WALL_THICKNESS,
                    totalWidth + (2 * WALL_THICKNESS),
                    totalHeight + (2 * WALL_THICKNESS));

            desenharLabirinto(g2d);
        }
    }

    // paleta de cores!
    private final List<Color> CORES = List.of(
            // Azul
            Color.decode("#1ba4d1"),  // escuro
            Color.decode("#44c8f5"),  // original
            Color.decode("#7ddcf9"),  // claro

            // Verde
            Color.decode("#1bb600"),  // escuro
            Color.decode("#26e400"),  // original
            Color.decode("#55ff33"),  // claro

            // Rosa
            Color.decode("#b30269"),  // escuro
            Color.decode("#e6038a"),  // original
            Color.decode("#ff339e"),  // claro

            // Amarelo
            Color.decode("#d4c102"),  // escuro
            Color.decode("#f8e002"),  // original
            Color.decode("#ffe733"),  // claro

            // Roxo
            Color.decode("#9502bf"),  // escuro
            Color.decode("#bf02f8"),  // original
            Color.decode("#d133ff"),  // claro

            // Laranja
            Color.decode("#d16502"),  // escuro
            Color.decode("#f87802"),  // original
            Color.decode("#ff9433")   // claro
    );

    public LabirintoGui(char[][] labirinto) {
        this.labirinto = labirinto;
        this.coresRegioes = new HashMap<>();
        this.regioes = new int[labirinto.length][labirinto[0].length];

        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[0].length; j++) {
                regioes[i][j] = -1;
            }
        }

        setTitle("Regiões do labirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // tamanho mínimo baseado no tamanho do labirinto
        int minWidth = Math.max(400, labirinto[0].length * MIN_CELL_SIZE + 2 * MIN_MARGIN);
        int minHeight = Math.max(400, labirinto.length * MIN_CELL_SIZE + 2 * MIN_MARGIN);
        setMinimumSize(new Dimension(minWidth, minHeight));

        JPanel painel = new LabirintoPanel();
        setContentPane(painel);

        // tamanho inicial ideal da janela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int prefWidth = Math.min(900, (int)(screenSize.width * 0.8));
        int prefHeight = Math.min(900, (int)(screenSize.height * 0.8));
        setSize(prefWidth, prefHeight);
        setLocationRelativeTo(null);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                painel.repaint();
            }
        });
    }

    private void calcularDimensoes(int width, int height) {
        // tamanho das células considerando as restrições de tela
        int availableWidth = width - (2 * MIN_MARGIN);
        int availableHeight = height - (2 * MIN_MARGIN);

        int maxCellWidthSize = availableWidth/labirinto[0].length;
        int maxCellHeightSize = availableHeight/labirinto.length;

        // limitar o tamanho das células entre MIN_CELL_SIZE e MAX_CELL_SIZE
        cellSize = Math.max(MIN_CELL_SIZE, Math.min(MAX_CELL_SIZE, Math.min(maxCellWidthSize, maxCellHeightSize)));

        // centralizar o labirinto
        int labirintoWidth = labirinto[0].length * cellSize;
        int labirintoHeight = labirinto.length * cellSize;

        marginX = (width - labirintoWidth) / 2;
        marginY = (height - labirintoHeight) / 2;
    }

    private void desenharLabirinto(Graphics2D g2d) {
        // ajustando a espessura da linha conforme a tela
        float strokeWidth = Math.max(1.0f, Math.min(WALL_THICKNESS, cellSize / 5.0f));
        g2d.setStroke(new BasicStroke(strokeWidth));

        // dsenhar regiões
        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[0].length; j++) {
                int x = marginX + (j * cellSize);
                int y = marginY + (i * cellSize);

                if (regioes[i][j] != -1)
                    colorirRegiao(g2d, regioes[i][j], x, y);
            }
        }

        // paredes com espessura proporcional
        g2d.setColor(Color.BLACK);

        // paredes externas
        int totalWidth = labirinto[0].length * cellSize;
        int totalHeight = labirinto.length * cellSize;

        g2d.draw(new Line2D.Double(marginX, marginY, marginX + totalWidth, marginY));
        g2d.draw(new Line2D.Double(marginX + totalWidth, marginY, marginX + totalWidth, marginY + totalHeight));
        g2d.draw(new Line2D.Double(marginX, marginY + totalHeight, marginX + totalWidth, marginY + totalHeight));
        g2d.draw(new Line2D.Double(marginX, marginY, marginX, marginY + totalHeight));

        // paredes internas
        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[0].length; j++) {
                int x = marginX + (j * cellSize);
                int y = marginY + (i * cellSize);
                char valor = labirinto[i][j];

                if (Character.isLetterOrDigit(valor))
                    desenharParedes(g2d, valor, x, y);
            }
        }
    }

    private void colorirRegiao(Graphics2D g2d, int regiao, int x, int y) {
        Color cor = coresRegioes.computeIfAbsent(regiao, k -> {
            int indice = Math.abs(k) % CORES.size();
            return CORES.get(indice);
        });

        g2d.setColor(cor);
        g2d.fillRect(x, y, cellSize, cellSize);
        g2d.setColor(Color.BLACK);
    }

    private void desenharParedes(Graphics2D g2d, char valor, int x, int y) {
        int hex = Character.digit(valor, 16);
        if (hex < 0) return;

        String binario = String.format("%4s", Integer.toBinaryString(hex))
                .replace(' ', '0');

        if (binario.charAt(0) == '1')
            g2d.draw(new Line2D.Double(x, y, x + cellSize, y));

        if (binario.charAt(1) == '1')
            g2d.draw(new Line2D.Double(x + cellSize, y, x + cellSize, y + cellSize));

        if (binario.charAt(2) == '1')
            g2d.draw(new Line2D.Double(x, y + cellSize, x + cellSize, y + cellSize));

        if (binario.charAt(3) == '1')
            g2d.draw(new Line2D.Double(x, y, x, y + cellSize));
    }

    public void setRegiao(int i, int j, int regiao) {
        if (i >= 0 && i < regioes.length && j >= 0 && j < regioes[0].length) {
            regioes[i][j] = regiao;
            repaint();
        }
    }

    public void atualizarRegiao(int[][] novosValores) {
        for (int i = 0; i < novosValores.length; i++) {
            for (int j = 0; j < novosValores[0].length; j++)
                regioes[i][j] = novosValores[i][j];
        }
        repaint();
    }

    public void atualizarLabirinto() {
        repaint();
    }
}