package lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class lab2 extends JComponent implements ActionListener {
    private double scale;
    private Color color;
    private Timer timer;
    private int matrixSize;
    private int[][] liveMatrix;

    public lab2(Color color, int delay, int matrixSize) {
        this.matrixSize = 9; //matrixSize;
        enterMatrix();
        scale = 1.0;
        timer = new Timer(delay, this);
        this.color = color;
        setPreferredSize(new Dimension(this.matrixSize * 100, this.matrixSize * 100));
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(-10, -10, 2000, 2000);
        int c = 450;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        int width = matrixSize * 100;
        int height = matrixSize * 100;
        g.fillRect(0, 0, width, height);
        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, width + 1, height + 1);
        for (int i = 1; i < matrixSize; i++) { // рисование сетки
            g2d.drawLine(0, 100 * i, 100 * 100, 100 * i); //hor lines
            g2d.drawLine(100 * i, 0, 100 * i, 100 * 100); //ver lines
        }
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) { // рисование текущего состояния
                if (liveMatrix[i][j] == 1)
                    g2d.fillRect(i * 100, j * 100, 100, 100);
            }
        }
        //checkAliveCells();
        checkAliveCellsInf();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.scale(scale, scale);
    }

    private int getValueAt(int x, int y) {
        if (0 <= x && x < liveMatrix.length && 0 <= y && y < liveMatrix[x].length)
            return liveMatrix[x][y];
        return 0;
    }

    private int getValueAtInf(int x, int y) {
        if(x<0) x=matrixSize-1;
        if(x==matrixSize) x=0;
        if(y==matrixSize) y=0;
        if(y<0) y=matrixSize-1;
        return liveMatrix[x][y];
    }

    private void checkAliveCellsInf() {
        int[][] newState = liveMatrix.clone();
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) { // просмотр соседей
                int nearAliveCount = 0;
                if (getValueAtInf(i + 1, j + 1) == 1) nearAliveCount++;
                if (getValueAtInf(i + 1, j - 1) == 1) nearAliveCount++;
                if (getValueAtInf(i - 1, j + 1) == 1) nearAliveCount++;
                if (getValueAtInf(i - 1, j - 1) == 1) nearAliveCount++;
                if (getValueAtInf(i, j - 1) == 1) nearAliveCount++;
                if (getValueAtInf(i, j + 1) == 1) nearAliveCount++;
                if (getValueAtInf(i + 1, j) == 1) nearAliveCount++;
                if (getValueAtInf(i - 1, j) == 1) nearAliveCount++;

                if (nearAliveCount <= 1) newState[i][j] = 0;
                else if (nearAliveCount >= 3) newState[i][j] = 0;
                else newState[i][j] = 1;
            }
        }
        liveMatrix = newState;
    }

    private void checkAliveCells() {
        int[][] newState = liveMatrix.clone();
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) { // просмотр соседей
                int nearAliveCount = 0;
                if (getValueAt(i + 1, j + 1) == 1) nearAliveCount++;
                if (getValueAt(i + 1, j - 1) == 1) nearAliveCount++;
                if (getValueAt(i - 1, j + 1) == 1) nearAliveCount++;
                if (getValueAt(i - 1, j - 1) == 1) nearAliveCount++;
                if (getValueAt(i, j - 1) == 1) nearAliveCount++;
                if (getValueAt(i, j + 1) == 1) nearAliveCount++;
                if (getValueAt(i + 1, j) == 1) nearAliveCount++;
                if (getValueAt(i - 1, j) == 1) nearAliveCount++;

                if (nearAliveCount <= 1) newState[i][j] = 0;
                else if (nearAliveCount >= 3) newState[i][j] = 0;
                else newState[i][j] = 1;
            }
        }
        liveMatrix = newState;
    }

    private void enterMatrix() {
        this.liveMatrix = new int[][]{
                {0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 1, 1, 0},
                {1, 1, 0, 0, 0, 1, 0, 0, 1},
                {0, 1, 1, 1, 0, 1, 0, 0, 0}
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("lab2");
                JPanel panel = new JPanel();
                final lab2 g = new lab2(Color.green, 1000, 5);

                panel.add(g);
                frame.getContentPane().add(panel);
                final JButton button = new JButton("Start");
                button.addActionListener(new ActionListener() {
                    private boolean pulsing = false;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pulsing) {
                            pulsing = false;
                            g.stop();
                            button.setText("Start");
                        } else {
                            pulsing = true;
                            g.start();
                            button.setText("Stop");
                        }
                    }
                });
                panel.add(button);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(g.matrixSize * 100 + 400, g.matrixSize * 100 + 100);
                frame.setVisible(true);
            }
        });
    }
}
