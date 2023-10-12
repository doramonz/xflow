package com.nhnacademy.aiot.Node.TCPServer;

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
                    for (Client client : ClientManager.getInstance().getClients()) {
                        client.send((String) message.getData()+"\n");
                    }
                } catch (InterruptedException e) {
                    log(e.getMessage());
                }
            }
        }
    }

}