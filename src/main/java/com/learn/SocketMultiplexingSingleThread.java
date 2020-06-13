package com.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketMultiplexingSingleThread {

    // NIO nonblocking socket网络，内核机制
    // NIO new io jdk（channel, bytebuffer, selector多路复用器）

    private ServerSocketChannel server = null;
    private Selector selector = null;
    int port = 9090;

    public void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        initServer();
        System.out.println("server started.");
        try {
            while(true){
                while(selector.select(0) > 0) { // 问内核有没有事件
                    Set<SelectionKey> selectionKeys = selector.selectedKeys(); // 从多路复用器去除有效的key
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()) {
                            acceptHandler(key);
                        } else if(key.isReadable()) {
                            System.out.println("用户数据到达就会触发， 但是何时会疯狂触发呢？");
                            readHandler(key);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            client.register(selector, SelectionKey.OP_READ, buffer);
            System.out.println("----------------------------------------");
            System.out.println("new client accepted: " + client.getRemoteAddress());
            System.out.println("----------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readHandler(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        try {
           while (true) {
               read = client.read(buffer);
               if(read > 0) {
                   buffer.flip();
                   while(buffer.hasRemaining()) {
                       client.write(buffer);
                   }
                   buffer.clear();
               } else if(read == 0) {
                   break;
               } else { // 如果没有这段，服务端泡在linux环境中时，当客户端暴力断开连接后，上面的 readHandler(key) 会一直被调用。
                   client.close();
                   break;
               }
           }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SocketMultiplexingSingleThread service = new SocketMultiplexingSingleThread();
        service.start();
    }
}
