import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RhythmGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int COLUMN_COUNT = 4;
    private static final int COLUMN_WIDTH = WIDTH / COLUMN_COUNT;
    private static final int NOTE_SPEED = 5;
    private static final int HIT_RANGE = 20;

    private List<Note> notes;
    private int score;
    private boolean[] keyStates;

    public RhythmGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        notes = new ArrayList<>();
        score = 0;
        keyStates = new boolean[COLUMN_COUNT];

        Timer timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 20, 30);

        for (int i = 0; i < COLUMN_COUNT; i++) {
            g.setColor(Color.WHITE);
            g.fillRect(i * COLUMN_WIDTH, 0, COLUMN_WIDTH, HEIGHT);
        }

        g.setColor(Color.RED);
        g.fillRect(0, HEIGHT - 30, WIDTH, 2);

        for (Note note : notes) {
            note.draw(g);
        }
    }

    public void update() {
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            note.move();

            if (note.getY() >= HEIGHT) {
                notes.remove(note);
                i--;
                score--;
            } else if (note.getY() >= HEIGHT - 30 && keyStates[note.getColumn()]) {
                notes.remove(note);
                i--;
                score++;
            }
        }

        repaint();
    }

    public void addNote() {
        Random random = new Random();
        int column = random.nextInt(COLUMN_COUNT);
        Note note = new Note(column * COLUMN_WIDTH);
        notes.add(note);
    }

    public int getActiveColumn() {
        for (int i = 0; i < COLUMN_COUNT; i++) {
            if (keyStates[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();

        Random random = new Random();
        if (random.nextInt(100) < 3) {
            addNote();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A) {
            keyStates[0] = true;
        } else if (keyCode == KeyEvent.VK_S) {
            keyStates[1] = true;
        } else if (keyCode == KeyEvent.VK_K) {
            keyStates[2] = true;
        } else if (keyCode == KeyEvent.VK_L) {
            keyStates[3] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A) {
            keyStates[0] = false;
        } else if (keyCode == KeyEvent.VK_S) {
            keyStates[1] = false;
        } else if (keyCode == KeyEvent.VK_K) {
            keyStates[2] = false;
        } else if (keyCode == KeyEvent.VK_L) {
            keyStates[3] = false;
        }
    }

    private class Note {
        private int x;
        private int y;
        private int column;

        public Note(int x) {
            this.x = x;
            this.y = 0;
            this.column = x / COLUMN_WIDTH;
        }

        public int getY() {
            return y;
        }

        public int getColumn() {
            return column;
        }

        public void move() {
            y += NOTE_SPEED;
        }

        public void draw(Graphics g) {
            g.setColor(Color.GREEN);
            g.fillRect(x + 2, y, COLUMN_WIDTH - 4, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rhythm Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new RhythmGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
