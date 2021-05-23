package org.unimelb.whiteboard.client;

import com.alibaba.fastjson.JSONObject;
import org.unimelb.whiteboard.client.lobby.LobbyWindow;
import org.unimelb.whiteboard.client.login.LoginWindow;
import org.unimelb.whiteboard.client.remote.IRemoteApp;
import org.unimelb.whiteboard.client.remote.IRemoteDoor;
import org.unimelb.whiteboard.client.remote.RemoteApp;
import org.unimelb.whiteboard.client.util.Execute;
import org.unimelb.whiteboard.client.whiteboard.ClientWhiteBoard;
import org.unimelb.whiteboard.client.whiteboard.ServerWhiteBoard;
import org.unimelb.whiteboard.client.whiteboard.SharedWhiteBoard;
import org.unimelb.whiteboard.standard.StateCode;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.util.Map;

public class Client {
    public static final int TIMEOUT = 600;
    public Map<Integer, String> roomList = null;
    private LoginWindow loginWindow = null;
    private LobbyWindow lobbyWindow = null;
    private String ip = "127.0.0.1";
    private String userId = "";
    private String serverIp = "";
    private int serverPort = -1;
    private SharedWhiteBoard sharedWhiteBoard = null;
    private int registryPort;
    private Registry registry;
    private IRemoteApp remoteApp;
    private ClientWhiteBoard tempClientWhiteBoard = null;
    private IRemoteDoor tempRemoteDoor;
    private String CurrentSavePath;

    public Client() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }


    /**
     * get & set server Ip and Port
     */
    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int port) {
        this.serverPort = port;
    }


    /**
     * get & set userId
     */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Run the program.
     */
    public void run() {
        loginWindow.getFrame().setVisible(true);
    }

    public IRemoteDoor getTempRemoteDoor() {
        return tempRemoteDoor;
    }

    public void setTempRemoteDoor(IRemoteDoor tempRemoteDoor) {
        this.tempRemoteDoor = tempRemoteDoor;
    }

    public Registry getRegistry() {
        return registry;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public String getIp() {
        return ip;
    }


    /**
     * When user completes sign in, use this method to switch to Lobby.
     */
    public void openLobby() {
        if (lobbyWindow == null)
            lobbyWindow = new LobbyWindow(this);

        lobbyWindow.setWaitDialogVisible(false);
        lobbyWindow.setBeKickedDialogVisible(false);

        loginWindow.getFrame().setVisible(false);

        unbindAndSetNull();

        lobbyWindow.getController().refreshRoomsList();
        lobbyWindow.getFrame().setVisible(true);
    }

    /**
     * next time when user create a whiteboard, they can be bind.
     */
    public void unbindAndSetNull() {
        try {
            registry.unbind("um");
            registry.unbind("paint");
            registry.unbind("door");
        } catch (NotBoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sharedWhiteBoard != null) {
            sharedWhiteBoard.getWindow().getFrame().setVisible(false);
            sharedWhiteBoard = null;
        }
        tempClientWhiteBoard = null;
        tempRemoteDoor = null;
    }

    /**
     * open whiteBoard.
     */
    public void openWhiteBoard() {
        lobbyWindow.setWaitDialogVisible(false);
        lobbyWindow.setBeKickedDialogVisible(false);

        lobbyWindow.getFrame().setVisible(false);
        loginWindow.getFrame().setVisible(false);
        sharedWhiteBoard.getWindow().getFrame().setVisible(true);
    }

    /**
     * open Login window.
     */
    public void openLogin(Boolean isCentralServerCrush) {
        if (lobbyWindow != null) {
            lobbyWindow.getFrame().setVisible(false);
        }

        if (sharedWhiteBoard != null) {
            sharedWhiteBoard.getWindow().getFrame().setVisible(false);
        }

        loginWindow.getFrame().setVisible(true);

        if (isCentralServerCrush) {
            JOptionPane.showMessageDialog(loginWindow.getFrame(), "Can't connect to the Server. Please try again.");
        }
    }


    /**
     * Create a room and register in the server.
     */
    public void createRoom(String roomName, String password) {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.ADD_ROOM);
        reqJSON.put("roomName", roomName);
        reqJSON.put("password", password);
        reqJSON.put("hostName", userId);
        reqJSON.put("hostIp", ip);
        reqJSON.put("hostPort", registryPort);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getInteger("state");
        if (state == StateCode.SUCCESS) {
            sharedWhiteBoard = new ServerWhiteBoard(this);
            sharedWhiteBoard.setRoomId(resJSON.getInteger("roomId"));
            openWhiteBoard();
        } else {
            System.out.println("Fail to create room.");
        }
    }

    public void joinRoom(String hostIp, int chatPort) {
        tempClientWhiteBoard.createChatClient(hostIp, chatPort);
        sharedWhiteBoard = tempClientWhiteBoard;
        openWhiteBoard();
    }


    public void createTempClientWhiteBoard(String hostId, String hostIp, int hostRegisterPort) {
        tempClientWhiteBoard = new ClientWhiteBoard(this, hostId, hostIp, hostRegisterPort);
    }

    public LobbyWindow getLobbyWindow() {
        return lobbyWindow;
    }

    public void pullRemoteRoomList() {
        // sent request to central server to gain roomList
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.GET_ROOM_LIST);
        JSONObject resJson = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJson.getIntValue("state");
        if (state == StateCode.SUCCESS) {
            roomList = (Map<Integer, String>) resJson.get("roomList");
        } else {
            openLogin(true);
        }
    }

    public int register() {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.ADD_USER);
        reqJSON.put("userId", userId);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getIntValue("state");
        return state;
    }

    public void removeUser() {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.REMOVE_USER);
        reqJSON.put("userId", userId);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getIntValue("state");
    }

    public void removeRoom() {
        int roomId = sharedWhiteBoard.getRoomId();
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.REMOVE_ROOM);
        reqJSON.put("roomId", roomId);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getIntValue("state");
    }

    public String getCurrentSavePath() {
        return CurrentSavePath;
    }

    public void setCurrentSavePath(String path) {
        this.CurrentSavePath = path;
    }

    private void init() {
        initRMI();
        loginWindow = new LoginWindow(this);
    }

    private void initRMI() {
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.tcp.responseTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.tcp.readTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.connectionTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.proxy.connectTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.tcp.handshakeTimeout", String.valueOf(TIMEOUT));

            RMISocketFactory.setSocketFactory(new RMISocketFactory() {
                public Socket createSocket(String host, int port) throws IOException {
                    Socket socket = new Socket();
                    socket.setSoTimeout(TIMEOUT);
                    socket.setSoLinger(false, 0);
                    socket.connect(new InetSocketAddress(host, port), TIMEOUT);
                    return socket;
                }

                public ServerSocket createServerSocket(int port) throws IOException {
                    return new ServerSocket(port);
                }
            });

            // Get a random port (Available one).
            ServerSocket registrySocket = new ServerSocket(0);
            registryPort = registrySocket.getLocalPort();
            registrySocket.close();

            // Start RMI registry
            LocateRegistry.createRegistry(registryPort);
            registry = LocateRegistry.getRegistry(ip, registryPort);

            // Create a remote user manager
            remoteApp = new RemoteApp(this);
            registry.bind("app", remoteApp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
