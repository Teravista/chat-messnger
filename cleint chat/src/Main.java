import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        SocketChannel client;
        ByteBuffer buffer;
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            buffer = ByteBuffer.allocate(256);
            System.out.println("connected");
            reciver reciver = new reciver(client);
            Thread thread = new Thread(reciver);
            thread.start();

            while(true)
            {
                Scanner scanner = new Scanner(System.in);
                String str = scanner.nextLine();
                buffer=ByteBuffer.wrap(str.getBytes());
                client.write(buffer);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }
}