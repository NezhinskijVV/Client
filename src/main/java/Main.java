import java.io.*;
import java.net.Socket;

public class Main {
    public final static String IP = "localhost";
    public final static int PORT = 8290;

    public static void main(String[] args) {
        System.out.println("Hi, client!");
        try (BufferedReader console = new BufferedReader(
                new InputStreamReader(System.in));
        ) {
            Socket socket = new Socket(IP, PORT);
            new Thread(() -> {
                try {
                    BufferedReader serverReader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        System.out.println(serverReader.readLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String message = console.readLine();
                System.out.println("I will send " + message);

                Thread t = new Thread(() -> {
                    try {
                        PrintWriter writer = new PrintWriter(socket.getOutputStream());
                        writer.println(message);
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                t.start();
                t.join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
