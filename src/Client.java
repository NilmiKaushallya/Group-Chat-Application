import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(String serverAddress, int serverPort, String username) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Send the username to the server
        sendMessage(username);
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage() throws IOException {
        return bufferedReader.readLine();
    }

    public void close() {
        try {
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Edit: Provide the server address, port, and username
            Client client = new Client("localhost", 1234, "YourUsername");
            // Edit: Implement the necessary functionality here, such as sending and receiving messages
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
