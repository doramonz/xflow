package com.nhnacademy.aiot.Node.TCPServer;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SocketOutNode extends ActiveNode {
    private NodeConnector[] inputConnectors;

    public SocketOutNode(int count) {
        super();
        inputConnectors = new NodeConnector[count];
    }

    public void connectInput(int index, NodeConnector port) {
        inputConnectors[index] = port;
    }

    public void disconnectInput(int index) {
        inputConnectors[index] = null;
    }

    @Override
    public void preProcess() {

    }

    @Override
    public void postProcess() {

    }

    @Override
    public void process() {
        for (NodeConnector port : inputConnectors) {
            if (port != null) {
                Message message;
                try {
                    message = port.pop();
                    if (message == null) {
                        continue;
                    }
                    if (message.json.has("ip-port")) {
                        JSONParser parser = new JSONParser();
                        String destination = message.json.getString("ip-port");
                        Client client = ClientManager.getInstance().getClient(destination);
                        client.send("HTTP/1.1 " + "200 OK" + "\n");
                        client.send("Access-Control-Allow-Origin: *" + "\n");
                        client.send("Content-type: text/json; charset=UTF-8" + "\n");
                        Object data = parser.parse(message.getData());
                        client.send("Content-Length: " + data.toString().length() + "\n" + "\n");
                        log(data.toString());
                        client.flush();
                        client.send(data + "\n" + "\n");
                        client.flush();
                    }
                } catch (InterruptedException e) {
                    log(e.getMessage());
                } catch (ParseException e) {
                    log(e.getMessage());
                }
            }
        }
    }

}

// JSONObject data = ((JsonMessage) message).getPayLoad();
// writer.write("HTTP/1.1 " + "200 OK" + "\n");
// writer.write("Access-Control-Allow-Origin: *" + "\n");
// writer.write("Content-type: text/json; charset=UTF-8" + "\n");
// writer.write("Content-Length: " + data.toString().length() + "\n" + "\n");
// writer.flush();
// writer.write(data.toString() + "\n");
// writer.flush();