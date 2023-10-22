import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

class InputDataHandler implements Runnable {
    private final BufferedReader handler;
    public InputDataHandler(BufferedReader handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        String data;
        try {
            while ((data = this.handler.readLine()) != null) {
                if (data.equals("exit()")) {
                    this.handler.close();
                    break;
                }
                System.out.println("client: " + data);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }
}
class Server {
    private int port;
    private Socket con;
    private ServerSocket serverSocket;
    private PrintWriter outDataHandler;
    private InputDataHandler inDataHandler;
    public Server() {
        this(3031);
    }

    public Server(
            int port
    ) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Servidor rodando no endereço: " + this.getLocalIp() + ":" + this.port);
            this.con = serverSocket.accept();
            System.out.println(this.con.getLocalAddress().getHostAddress() + " conectado");
            this.outDataHandler = new PrintWriter(this.con.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.con.getInputStream()));
            this.inDataHandler = new InputDataHandler(in);
            new Thread(this.inDataHandler).start();
//            String greeting = this.inDataHandler.readLine();
//            System.out.println(greeting);
//            this.outDataHandler.println("Ok!");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public String getLocalIp() throws UnknownHostException, SocketException {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), this.port);
            String ip = socket.getLocalAddress().getHostAddress();
            socket.close();
            return ip;
    }

    public void stopServer() {
        try {
            this.outDataHandler.close();
            this.con.close();
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String msg) {
        this.outDataHandler.println(msg);
    }

    public String getIp() throws UnknownHostException {
        return Inet4Address.getLocalHost().getHostAddress();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        try {
            System.out.println("Servidor rodando no endereço: " + this.getLocalIp() + ":" + this.port);
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }
}

public class ServerRun {
    private static final Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        Server server = new Server();
        String msg;
        while (true) {
            msg = ServerRun.input.nextLine();
            server.sendMessage(msg);
            if (msg.equals("exit()")) {
                server.stopServer();
                break;
            }
        }
    }
}
