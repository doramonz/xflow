package com.nhnacademy.aiot.Node.TCPClient;

import java.util.logging.Logger;

public class Node {
    Logger logger;

    public Node() {
        super();
        logger = Logger.getLogger("Node");
    }

    public void log(String message) {
        logger.info(message);
    }
}
