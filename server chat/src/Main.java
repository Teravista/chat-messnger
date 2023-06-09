import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 8080));
        serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        Map<SocketChannel, List<ByteBuffer>> socketMessegMap
                = new HashMap<>();
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    register(selector, serverSocket,socketMessegMap);
                }
                if (key.isReadable()) {
                    readAndRespond(buffer, key,socketMessegMap);
                }
                iter.remove();
            }
        }

    }

    private static void readAndRespond(ByteBuffer buffer, SelectionKey keyy,Map<SocketChannel, List<ByteBuffer>> socketMessegMap) throws IOException
    {
        SocketChannel client = (SocketChannel) keyy.channel();

        int r = client.read(buffer);
        if (r == -1 || new String(buffer.array()).trim().equals("end")) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        }
        else {
            for (SocketChannel key: socketMessegMap.keySet()) {
                if (client!=key) {
                    List<ByteBuffer> tempList = socketMessegMap.get(key);
                    synchronized (tempList) {
                        tempList.add(buffer);
                        tempList.notify();
                    }
                }
            }
        }

    }
    private static void register(Selector selector, ServerSocketChannel serverSocket,Map<SocketChannel, List<ByteBuffer>> socketMessegMap)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        List<ByteBuffer> listOfMesseges = new ArrayList<>();
        socketMessegMap.put(client,listOfMesseges);
        Sender sender = new Sender(client,listOfMesseges);
        new Thread(sender).start();
        client.register(selector, SelectionKey.OP_READ);
    }
}