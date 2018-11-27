package snake;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class Snake {

  private Gameplay gameplay;

    public static void main (String[] args) {


      JFrame obj = new JFrame();

      Gameplay gameplay = new Gameplay();

      obj.setBounds(10, 10, 564, 515);
      obj.setBackground(Color.DARK_GRAY);
      obj.setResizable(false);
      obj.setVisible(true);
      obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      obj.add(gameplay);
      obj.setTitle("Snake");


  }
}
