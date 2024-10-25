package controller;

import java.util.List;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import model.Packet;

/**
 *
 * @author ADMIN
 */
public class ClientControl {
    private Socket mySocket;
    private String serverHost = "localhost";
    private int serverPort = 8888;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Thread listenThread;
    private boolean isListening = true;
    private List<PacketListener> listeners = new CopyOnWriteArrayList<>();
    
    public ClientControl() {
    }
    
    public Socket openConnection() {
        try {
            mySocket = new Socket(serverHost, serverPort);
            oos = new ObjectOutputStream(mySocket.getOutputStream());
            ois = new ObjectInputStream(mySocket.getInputStream());
            listenToServer();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mySocket;
    }
    
    private void listenToServer() {
        listenThread = new Thread(() -> {
            try {
                while (isListening) {
                    try {
                        Packet packet = (Packet) ois.readObject();
                        if (packet != null) {
                            handleData(packet);
                        }
                    } catch (InterruptedIOException e) {
                        if (!isListening) {
                            System.out.println("Thread was interrupted.");
                            break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } finally {
                System.out.println("Stopped listening to server.");
            }
        });
        listenThread.start();
    }

    
    public void handleData(Packet packet) {
        for (PacketListener listener : listeners) {
            listener.onPacketReceived(packet);
        }
    }
    
    public boolean sendData(Packet packet) {
        try {
            oos.writeObject(packet);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public Object recieveData() {
        try {
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void stopListening() {
        isListening = false;
        if (listenThread != null && listenThread.isAlive()) {
            listenThread.interrupt();
            try {
                listenThread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted while waiting for join.");
                Thread.currentThread().interrupt();
            }
        }
    }

    
    public boolean closeConnection() {
        try {
            mySocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void addPacketListener(PacketListener listener) {
        listeners.add(listener);
    }

    public void removePacketListener(PacketListener listener) {
        listeners.remove(listener);
    }
}
