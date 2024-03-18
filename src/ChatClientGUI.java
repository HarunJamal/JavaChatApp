import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;

    public ChatClientGUI(){
        super("Chat Application");
        

  // Prompt for user name
  String name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
  this.setTitle("Chat Application - " + name); // Set window title to include user name

  // Modify actionPerformed to include the user name and time stamp
  


        
        setSize(400,500);  //try getPreferredSize() , parameter is Dimension type (custom prolly in one of the imported classes)
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // replace with 3 and check result

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + name + ": " + textField.getText();
            client.sendMessage(message);
            textField.setText("");
          }
        });
        add(textField, BorderLayout.SOUTH);

      // Initialize and start the ChatClient
      try {
          this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
          client.startClient();
      } catch (IOException e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(this, "Error connecting to the server", "Connection error",
                  JOptionPane.ERROR_MESSAGE);
          System.exit(1);
      }
  }

  private void onMessageReceived(String message) {
      SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
          new ChatClientGUI().setVisible(true);
      });
  }
}
