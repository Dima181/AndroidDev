package ru.mirea.hristov.timeservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketUtils {
    private Socket socket;

    public SocketUtils(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
