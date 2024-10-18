import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener, java.awt.event.ActionListener {
   static final int SCREEN_HEIGHT = 600;
   static final int SCREEN_WIDTH = 600;
   static final int UNIT_SIZE = 10;
   static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
   static final int DELAY = 75;
   final int x[] = new int[GAME_UNITS]; // snakes x coordinates
   final int y[] = new int[GAME_UNITS]; // snakes y coordinates
   int bodyParts = 6;
   int eggsEaten = 0;
   int eggX;
   int eggY;
   char direction = 'R';
   boolean running = false;
   Timer timer;
   Random random;

   GamePanel() {
      random = new Random();
      this.setPreferredSize(new Dimension(SCREEN_HEIGHT, SCREEN_WIDTH));
      this.setBackground(Color.black);
      this.setFocusable(true);
      this.addKeyListener(new MyKeyAdapter());
      startGame();
   }

   public void startGame() {
      newEgg();
      running = true;
      timer = new Timer(DELAY, this);
      timer.start();

   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      draw(g);

   }

   public void draw(Graphics g) {
      if (running) {
         for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

         }

         g.setColor(Color.white);
         g.fillOval(eggX, eggY, UNIT_SIZE, UNIT_SIZE);

         for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
               g.setColor(Color.green);
               g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
               g.setColor(new Color(45, 180, 0));
               g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
         }

         g.setColor(Color.yellow);
         g.setFont(new Font("Arial", Font.BOLD, 30));
         FontMetrics metrics = getFontMetrics(g.getFont());
         g.drawString("SCORE: " + eggsEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + eggsEaten)) / 2,
               g.getFont().getSize());

      } else {
         gameOver(g);
      }
   }

   public void newEgg() {
      eggX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
      eggY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
   }

   public void move() {
      for (int i = bodyParts; i > 0; i--) {
         x[i] = x[i - 1];
         y[i] = y[i - 1];
      }

      switch (direction) {
         case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;

         case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;

         case 'L':
            x[0] = x[0] - UNIT_SIZE;

            break;
         case 'R':
            x[0] = x[0] + UNIT_SIZE;

            break;

         default:
            break;
      }
   }

   public void checkEgg() { // checkEgg=checkApple
      if ((x[0] == eggX) && (y[0] == eggY)) {
         bodyParts++;
         eggsEaten++;
         newEgg();

      }

   }

   public void checkCollisions() {
      // checks if head collides with body
      for (int i = bodyParts; i > 0; i--) {
         if ((x[0] == x[i]) && y[0] == y[i]) {
            running = false;
         }
      }
      // check if head touches left border
      if (x[0] < 0) {
         running = false;
      }
      // check if head touches right border
      if (x[0] > SCREEN_WIDTH) {
         running = false;
      }
      // check if head touches top border
      if (y[0] < 0) {
         running = false;
      }
      // check if head touches bottom border
      if (y[0] > SCREEN_HEIGHT) {
         running = false;
      }
      if (!running) {
         timer.stop();
      }

   }

   public void gameOver(Graphics g) {
      g.setColor(Color.red);
      g.setFont(new Font("Arial", Font.BOLD, 75));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString("GAME OVER ", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2,
            SCREEN_HEIGHT / 2);
      g.setColor(Color.white);
      g.setFont(new Font("Arial", Font.BOLD, 25));
      FontMetrics metric = getFontMetrics(g.getFont());
      g.drawString("SCORE: "
            + eggsEaten, (SCREEN_WIDTH - metric.stringWidth("GAME OVER")) / 2,
            SCREEN_HEIGHT / 2 + 75);
   }

   @Override
   public void actionPerformed(java.awt.event.ActionEvent e) {
      if (running) {
         move();
         checkEgg();
         checkCollisions();

      }
      repaint();
   }

   public class MyKeyAdapter extends KeyAdapter {
      @Override
      public void keyPressed(KeyEvent e) {
         switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
               if (direction != 'R') {
                  direction = 'L';
               }
               break;

            case KeyEvent.VK_RIGHT:
               if (direction != 'L') {
                  direction = 'R';
               }
               break;

            case KeyEvent.VK_UP:
               if (direction != 'D') {
                  direction = 'U';
               }
               break;

            case KeyEvent.VK_DOWN:
               if (direction != 'U') {
                  direction = 'D';
               }
               break;
         }
      }
   }

}
