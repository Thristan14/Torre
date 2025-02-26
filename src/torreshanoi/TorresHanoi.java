package torreshanoi;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TorresHanoi extends JPanel {
    private int numDiscos, numTorres;
    private Stack<Integer>[] torres;
    private int movimientoActual = 0;
    private List<int[]> movimientos;
    private Color[] colores;

    public TorresHanoi(int numDiscos, int numTorres) {
        this.numDiscos = numDiscos;
        this.numTorres = Math.max(numTorres, 3); // Asegurar al menos 3 torres
        this.torres = new Stack[this.numTorres];
        this.colores = new Color[numDiscos];
        generarColores();
        for (int i = 0; i < this.numTorres; i++) {
            torres[i] = new Stack<>();
        }
        for (int i = numDiscos; i > 0; i--) {
            torres[0].push(i); // Coloca todos los discos en la primera torre
        }
        this.movimientos = new ArrayList<>();
        resolverHanoi(numDiscos, 0, this.numTorres - 1);
        new Timer(500, e -> moverDisco()).start();
    }

    private void generarColores() {
        for (int i = 0; i < numDiscos; i++) {
            colores[i] = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        }
    }

    private void resolverHanoi(int n, int origen, int destino) {
        if (n == 0) return; // Caso base

        // Encuentra una torre auxiliar que no sea origen ni destino
        int auxiliar = -1;
        for (int i = 0; i < numTorres; i++) {
            if (i != origen && i != destino) {
                auxiliar = i;
                break;
            }
        }

        if (auxiliar == -1) return; // Si no hay torre auxiliar, termina

        // Mueve n-1 discos de origen a auxiliar, usando destino como auxiliar
        resolverHanoi(n - 1, origen, auxiliar);

        // Mueve el disco restante de origen a destino
        movimientos.add(new int[]{origen, destino});

        // Mueve n-1 discos de auxiliar a destino, usando origen como auxiliar
        resolverHanoi(n - 1, auxiliar, destino);
    }

    private void moverDisco() {
        if (movimientoActual < movimientos.size()) {
            int[] mov = movimientos.get(movimientoActual);
            int origen = mov[0];
            int destino = mov[1];

            if (!torres[origen].isEmpty()) {
                int disco = torres[origen].pop();
                if (torres[destino].isEmpty() || torres[destino].peek() > disco) {
                    torres[destino].push(disco);
                    movimientoActual++;
                    repaint();
                } else {
                    torres[origen].push(disco); // Devuelve el disco a la torre de origen
                    movimientoActual++; // Avanza al siguiente movimiento
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int baseY = height - 50;

        // Dibuja las torres
        for (int i = 0; i < numTorres; i++) {
            g.fillRect((i + 1) * width / (numTorres + 1) - 5, baseY - 200, 10, 200);
        }

        // Dibuja los discos
        for (int i = 0; i < numTorres; i++) {
            Stack<Integer> torre = torres[i];
            int xBase = (i + 1) * width / (numTorres + 1);
            int y = baseY;
            for (int disco : torre) {
                int discoWidth = disco * 20;
                g.setColor(colores[disco - 1]);
                g.fillRect(xBase - discoWidth / 2, y - 20, discoWidth, 20);
                g.setColor(Color.BLACK);
                g.drawRect(xBase - discoWidth / 2, y - 20, discoWidth, 20);
                y -= 20;
            }
        }
    }

    public static void main(String[] args) {
        int numDiscos = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de discos:"));
        int numTorres = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de torres:"));
        JFrame frame = new JFrame("Torres de Hanoi con múltiples torres");
        TorresHanoi panel = new TorresHanoi(numDiscos, numTorres);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}