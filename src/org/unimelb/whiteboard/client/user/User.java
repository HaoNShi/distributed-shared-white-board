package org.unimelb.whiteboard.client.user;

import java.io.Serializable;

public class User implements Serializable {
    public static final int MANAGER = 0;
    public static final int MEMBER = 1;
    public static final int APPLICANT = 2;

    // Personal info
    String userId;
    // User identity
    int identity;
    // Network Info
    String ip;
    int registerPort;
    int chatPort;

    public User(String userId, int identity, String ip, int registerPort, int chatPort) {
        this.userId = userId;
        this.identity = identity;
        this.ip = ip;
        this.registerPort = registerPort;
        this.chatPort = chatPort;
    }

    public String getUserId() {
        return userId;
    }

    public String getIp() {
        return ip;
    }

    public int getRegisterPort() {
        return registerPort;
    }

    public int getChatPort() {
        return chatPort;
    }
}
