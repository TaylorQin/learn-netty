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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketMultiplexingThreads {

    private ServerSocketChannel server = null;
    private Selector selector1 = null;
    private Selector selector2 = null;
    private Selector selector3 = null;
    int port = 9090;


    public static void main(String[] args) {
        SocketMultiplexingThreads service = new SocketMultiplexingThreads();
        service.initServer();
        NioThread t1 = new NioThread(service.selector1, 2);
        NioThread t2 = new NioThread(service.selector2);
        NioThread t3 = new NioThread(service.selector3);

        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
        t3.start();
        System.out.println("服务器启动了。。。。。。");
    }

    public void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            selector1 = Selector.open();
            selector2 = Selector.open();
            selector3 = Selector.open();
            server.register(selector1, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class NioThread extends Thread {
    Selector selector = null;
    static int selectors = 0;
    boolean boss = false;

    int id = 0;

    static BlockingQueue<SocketChannel>[] queue;
    static AtomicInteger idx =  new AtomicInteger();

    public NioThread(Selector selector, int threadNumber) {
        this.selector = selector;
        this.selectors = threadNumber;
        boss = true;
        queue = new LinkedBlockingDeque[selectors];
        for (int i = 0; i < threadNumber; i++) {
            queue[i] = new LinkedBlockingDeque<>();
        }
        System.out.println("Boss 启动");
    }

    public NioThread(Selector selector) {
        this.selector = selector;
        id = idx.getAndIncrement() % selectors;
        System.out.println("Worker: "+id+" 启动");
    }

    @Override
    public void run() {
        try {
            while (true) {
                while (selector.select(10) > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        }
                    }
                }

                if (!boss && !queue[id].isEmpty()) {
                    ByteBuffer buffer = ByteBuffer.allocate(4096);
                    SocketChannel client = queue[id].take();
                    client.register(selector, SelectionKey.OP_READ, buffer);
                    System.out.println("-----------------------------------------");
                    System.out.println("新客户端： "+client.socket().getPort()+"分配到Worker:"+id);
                    System.out.println("-----------------------------------------");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);

            int num = idx.getAndIncrement() % selectors;
            queue[num].add(client);

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
}
