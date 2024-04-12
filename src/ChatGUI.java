import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatGUI extends JFrame {
    private JTextPane chatArea;
    private JTextField messageInput;
    private JButton sendButton;
    private Client client;

    public ChatGUI(Client client) {
        this.client = client;

        setTitle("Group Chat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize Swing components
        chatArea = new JTextPane();
        chatArea.setEditable(false); // Prevent editing of chat history
        chatArea.setContentType("text/html"); // Set content type to HTML for formatting
        chatArea.setPreferredSize(new Dimension(300, 500)); // Set preferred size

        messageInput = new JTextField();
        sendButton = new JButton("Send");
        sendButton.setBackground(Color.DARK_GRAY); // Set background color of the send button to dark gray
        sendButton.setForeground(Color.WHITE); // Set foreground color (text color) of the send button to white

        // Layout
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        contentPane.add(inputPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);

        // Set background color of chat screen to cyan
        chatArea.setBackground(Color.CYAN);

        // Set preferred size to match a normal mobile screen size
        setSize(360, 640); // Typical dimensions for mobile screens (e.g., 360x640 pixels)

        // Event listeners
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Start listening for incoming messages
        listenForMessages();
    }

    // Method to send a message
    private void sendMessage() {
        String message = messageInput.getText();
        client.sendMessage(message);
        messageInput.setText(""); // Clear the input field after sending
    }

    // Method to update chat area with received message
    public void updateChatArea(String message) {
        appendToPane(chatArea, message + "\n", Color.BLACK);
    }

    // Method to append colored text to JTextPane
    private void appendToPane(JTextPane textPane, String message, Color color) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), message, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Method to listen for incoming messages from the client
    private void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = client.receiveMessage();
                        updateChatArea(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            // Prompt for the username and create the client
            String username = JOptionPane.showInputDialog("Enter your username for the group chat:");
            Client client = new Client("localhost", 1234, username); // Provide the username entered by the user
            // Create and display the GUI
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ChatGUI(client).setVisible(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

