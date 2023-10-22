import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientInputDataHandler implements Runnable {
    private final BufferedReader handler;
    public ClientInputDataHandler(BufferedReader handler) {
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
                System.out.println("server: " + data);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }
}

class Client {
    private Socket con;
    private PrintWriter outDataHandler;
    private ClientInputDataHandler inDataHandler;
    private String ip;
    private int port;

    public Client(String ip) {
        this(ip, 3031);
    }

    public Client(
            String ip,
            int port
    ) {
        this.ip = ip;
        this.port = port;
        try {
            this.con = new Socket(ip, port);
            System.out.println("Connected to " + ip + ":" + port);
            this.outDataHandler = new PrintWriter(this.con.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.con.getInputStream()));
            this.inDataHandler = new ClientInputDataHandler(in);
            new Thread(this.inDataHandler).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String msg) {
        this.outDataHandler.println(msg);
    }

    public void disconnect() {
        try {
            this.outDataHandler.close();
            this.con.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

public class ClientRun {
    private final static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.print("Digite o ip do servidor: ");
        String url = ClientRun.input.nextLine();
        String[] urlArr = url.split(":");
        Client client = new Client(urlArr[0], Integer.parseInt(urlArr[1]));
        String msg;
        while (true) {
            msg = ClientRun.input.nextLine();
            client.sendMessage(msg);
            if (msg.equals("exit()")) {
                client.disconnect();
                break;
            }
        }
    }
}
