import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static List<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) throws IOException{
        ServerSocket servsoc = new ServerSocket(6969);

        System.out.println("Sewer started waiting for client mesej");

        while(true){
            Socket clientsoc = servsoc.accept();
            System.out.println("Client connected" + clientsoc);

            // new thread for each new client
            ClientHandler clientThread = new ClientHandler(clientsoc, clients);
            clients.add(clientThread);
            new Thread(clientThread).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientsoc;
    private List<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException{
        this.clientsoc = socket;
        this.clients = clients;
        this.out = new PrintWriter(clientsoc.getOutputStream(), true); 
        this.in = new BufferedReader(new InputStreamReader(clientsoc.getInputStream()));
    }

    public void run(){
        try{
            String inputLine;
            while((inputLine = in.readLine()) != null){
                for (ClientHandler aClient : clients){
                    aClient.out.println(inputLine);
                }
            }
        } catch(IOException e){
            System.out.println("An error occured: " + e.getMessage());
        } finally {
            try{
                in.close();
                out.close();
                clientsoc.close();
            } catch (IOException e){
                e.printStackTrace();
            
            }
        }
    }
}
