package com.learn;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class SocketNIO {
    public static void main(String[] args) throws Exception {

        LinkedList<SocketChannel> clients = new LinkedList<SocketChannel>();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(9090));
        ssc.configureBlocking(false);// set channel as non-blocking

        while(true) {
            Thread.sleep(1000);
            SocketChannel client = ssc.accept();
            if(client == null) {
                System.out.println("null...");
            }else{
                client.configureBlocking(false);
                int port = client.socket().getPort();
                System.out.println("client...port:\t"+port);
                clients.add(client);
            }
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            for (SocketChannel sc:clients) {
                int num = sc.read(buffer);
                if(num>0) {
                    buffer.flip();
                    byte[] a = new byte[buffer.limit()];
                    buffer.get(a);
                    String b = new String(a);
                    System.out.println(sc.socket().getPort()+" : "+b);
                    buffer.clear();
                }
            }


        }

    }
}
