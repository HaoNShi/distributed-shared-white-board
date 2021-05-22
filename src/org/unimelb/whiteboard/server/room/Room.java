package org.unimelb.whiteboard.server.room;

public class Room {
    private final int roomId;
    private final String ipAddress;
    private final int port;
    private final String hostName;
    private final String roomName;
    private final String password;

    public Room(int roomId, String ipAddress, int port, String hostName, String roomName, String password) {
        this.roomId = roomId;
        this.ipAddress = ipAddress;
        this.port = port;
        this.hostName = hostName;
        this.roomName = roomName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPassword() {
        return password;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public int getRoomId() {
        return roomId;
    }
}
