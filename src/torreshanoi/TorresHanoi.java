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
    private boolean juegoTerminado = false;


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
        resolverHanoi(numDiscos, 0, this.numTorres - 1, 1);
        new Timer(500, e -> moverDisco()).start();
    }

    private void generarColores() {
        for (int i = 0; i < numDiscos; i++) {
            colores[i] = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        }
    }

    private void resolverHanoi(int n, int origen, int destino, int auxiliar) {
        if (n == 1) {
            movimientos.add(new int[]{origen, destino});
            return;
        }
        resolverHanoi(n - 1, origen, auxiliar, destino);
        movimientos.add(new int[]{origen, destino});
        resolverHanoi(n - 1, auxiliar, destino, origen);
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
        } else if (!juegoTerminado) {
            // Verifica si todos los discos están en la última torre
            if (torres[numTorres - 1].size() == numDiscos) {

                juegoTerminado = true; // Marca el juego como terminado
                // Muestra el mensaje de felicitación con el número de movimientos
                JOptionPane.showMessageDialog(this, 
                    "¡Lo has logrado!\nNúmero de movimientos: " + movimientoActual, 
                    "Felicidades", 
                    JOptionPane.INFORMATION_MESSAGE);

                juegoTerminado = true;
                JOptionPane.showMessageDialog(this, "¡Lo Has Logrado!", "Felicidades", JOptionPane.INFORMATION_MESSAGE);

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
        JFrame frame = new JFrame("Torres de Hanoi");
        TorresHanoi panel = new TorresHanoi(numDiscos, numTorres);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        //programación
    }
}