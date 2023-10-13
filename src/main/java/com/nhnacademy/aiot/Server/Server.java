package com.nhnacademy.aiot.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.nhnacademy.aiot.Node.DataServerNode;
import com.nhnacademy.aiot.Node.FillterNode;
import com.nhnacademy.aiot.Node.SocketInNode;
import com.nhnacademy.aiot.Node.SocketOutNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
    static String newLine = "\n";

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        int count = 0;
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            Socket client;

            while ((client = serverSocket.accept()) != null) {
                log.info("{} | PORT : {}", client.getInetAddress().getHostAddress(),
                        client.getPort());

                Socket dataServer = new Socket("ems.nhnacademy.com", 1880);

                SocketOutNode socketOutNode = new SocketOutNode(client);
                SocketInNode socketInNode = new SocketInNode(client);

                DataServerNode dataServerNode = new DataServerNode(dataServer);

                FillterNode serverFillterNode = new FillterNode();

                socketInNode.connect(0, dataServerNode.getPort(0));

                dataServerNode.connect(0, serverFillterNode.getPort(0));

                serverFillterNode.connect(0, socketOutNode.getPort(0));

                socketInNode.start();
                socketOutNode.start();
                dataServerNode.start();
                serverFillterNode.start();

                // JSONObject object = new JSONObject();
                // object.put("hello", "world!");
                // BufferedWriter writer =
                // new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                // writer.write("HTTP/1.1 " + "200 OK" + newLine);
                // writer.write("Server: Hyeonseon9's" + newLine);
                // writer.write("Date: " + new Date() + newLine);
                // writer.write("Content-type: text/html; charset=UTF-8" + newLine);
                // writer.write("Content-Length: " + object.toString().length() + newLine +
                // newLine);
                // writer.write(object + newLine);
                // writer.flush();
                // RNGNode rngNode = new RNGNode(3);
                // rngNode.getPort(0).put(new LongMessage(count++));
                // rngNode.connect(0, socketOutNode.getPort(0));
                // rngNode.start();
                // socketOutNode.start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}