package org.unimelb.whiteboard.client.User;

import org.unimelb.whiteboard.client.Chat.ChatPanel;
import org.unimelb.whiteboard.client.RMI.IRemoteApp;
import org.unimelb.whiteboard.client.RMI.IRemotePaint;
import org.unimelb.whiteboard.client.RMI.IRemoteUM;
import org.unimelb.whiteboard.client.WhiteBoard.ClientListScrollPanel;
import org.unimelb.whiteboard.client.WhiteBoard.PaintManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;


public class UserManager {
    private final Boolean isHost;
    // Host info
    private final User host;
    private final Map<String, IRemotePaint> guestRemotePaints;
    private final Map<String, IRemoteApp> guestRemoteApps;
    private final Map<String, IRemoteUM> guestRemoteUMs;
    // Visitors info
    private final Map<String, User> visitors;
    private final Map<String, IRemoteApp> visitorRemoteApps;
    // Use to refresh ui
    ClientListScrollPanel clsp;
    ChatPanel chatPanel;
    private PaintManager hostPaintManager;
    private int hostChatPort;
    private IRemotePaint hostRemotePaint;
    private IRemoteUM hostRemoteUM;
    private IRemoteApp hostRemoteApp;
    // Guests info
    private Map<String, User> guests;

    public UserManager(Boolean isHost, String hostId, String hostIp, int registerPort, int chatPort) {
        this.isHost = isHost;

        this.host = new User(hostId, User.HOST, hostIp, registerPort, chatPort);
        if (!isHost) {
            try {
                Registry registry = LocateRegistry.getRegistry(hostIp, registerPort);
                // Registry registry = LimitedTimeRegistry.getLimitedTimeRegistry(hostIp, registerPort, 1000);
                hostRemotePaint = (IRemotePaint) registry.lookup("paint");
                hostRemoteUM = (IRemoteUM) registry.lookup("um");
                hostRemoteApp = (IRemoteApp) registry.lookup("app");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        guests = new HashMap<>();
        guestRemotePaints = new HashMap<>();
        guestRemoteUMs = new HashMap<>();
        guestRemoteApps = new HashMap<>();

        visitors = new HashMap<>();
        visitorRemoteApps = new HashMap<>();
    }

    /**
     * Get the host chat port.
     */
    public int getHostChatPort() {
        return hostChatPort;
    }

    /**
     * Set the host's chat port.
     */
    public void setHostChatPort(int chatPort) {
        this.hostChatPort = chatPort;
    }

    public void setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

    /**
     * This method is for host, set the host paint manager.
     */
    public void setHostPaintManager(PaintManager paintManager) {
        if (isHost)
            this.hostPaintManager = paintManager;
    }

    /**
     * Remove all the guest and visitor.
     */
    public void clear() {
        for (IRemoteApp remoteApp : guestRemoteApps.values()) {
            try {
                remoteApp.askOut();
            } catch (Exception e) {
                System.out.println("Discover a guest has network problem when asking him out.");
            }
        }

        for (IRemoteApp remoteApp : visitorRemoteApps.values()) {
            try {
                remoteApp.askOut();
            } catch (Exception e) {
                System.out.println("Discover a visitor has network problem when asking him out.");
            }
        }

        guestRemoteApps.clear();
        guestRemotePaints.clear();
        guestRemoteUMs.clear();
        guests.clear();

        visitorRemoteApps.clear();
        visitors.clear();
    }

    /**
     * Check whether is the user manager belongs to host.
     */
    public Boolean isHost() {
        return isHost;
    }

    /**
     * Get remote UM.
     */
    public IRemoteUM getHostRemoteUM() {
        return hostRemoteUM;
    }

    /**
     * Add a visitor to guest list, open the door for a visitor.
     */
    public void addGuest(String guestId) {
        System.out.println("Allow " + guestId + " enter.");
        // add guest
        User guest = visitors.get(guestId);
        guests.put(guestId, guest);
        guestRemoteApps.put(guestId, visitorRemoteApps.get(guestId));
        try {
            Registry clientRegistry = LocateRegistry.getRegistry(guest.getIp(), guest.getRegisterPort());
            // Registry clientRegistry = LimitedTimeRegistry.getLimitedTimeRegistry(guest.getIp(), guest.getRegisterPort(), 1000);
            IRemoteApp guestRemoteApp = (IRemoteApp) clientRegistry.lookup("app");
            guestRemoteApp.askIn(host.getIp(), hostChatPort);

            IRemotePaint remotePaint = (IRemotePaint) clientRegistry.lookup("paint");
            guestRemotePaints.put(guestId, remotePaint);
            if (hostPaintManager != null) {
                remotePaint.setHistory(hostPaintManager.getPaintHistory());
            }

            IRemoteUM remoteUM = (IRemoteUM) clientRegistry.lookup("um");
            guestRemoteUMs.put(guestId, remoteUM);

            // delete visitor
            visitors.remove(guestId);
            visitorRemoteApps.remove(guestId);
        } catch (Exception e) {
            removeGuest(guestId);
            visitors.remove(guestId);
            visitorRemoteApps.remove(guestId);
            chatPanel.appendText("Can't connect to guest " + guestId + ", Remove.\n");
            System.err.println("Can't connect to guest " + guestId + ", Remove.");
            // e.printStackTrace();
        }

        // set remote user manager
        for (String key : guestRemoteUMs.keySet()) {
            try {
                guestRemoteUMs.get(key).setGuests(guests);
            } catch (RemoteException e) {
                System.err.println("Can't connect to guest " + key + ", Remove.");
                removeGuest(key);
                //e.printStackTrace();
            }
        }

        // refresh ui.
        if (clsp != null) {
            clsp.updateUserList();
        }
    }

    /**
     * Remove a guest from guest list.
     */
    public void removeGuest(String guestId) {
        guests.remove(guestId);
        guestRemoteUMs.remove(guestId);
        guestRemotePaints.remove(guestId);
        guestRemoteApps.remove(guestId);

        // set remote user manager
        for (String key : guestRemoteUMs.keySet()) {
            try {
                guestRemoteUMs.get(key).setGuests(guests);
            } catch (RemoteException e) {
                System.err.println("Can't connect to guest " + key + ", Remove.");
                removeGuest(key);
                //e.printStackTrace();
            }
        }

        if (chatPanel != null)
            chatPanel.appendText("Guest " + guestId + " leaves!\n");

        // refresh ui.
        if (clsp != null) {
            clsp.updateUserList();
        }
    }

    /**
     * Kick a guest out of the room.
     */
    public void kickGuest(String guestId) {
        System.out.println("Kick " + guestId + " out.");
        try {
            IRemoteApp remoteApp = guestRemoteApps.get(guestId);
            remoteApp.askOut();
        } catch (Exception e) {
            System.err.println("Can't connect to guest " + guestId + ", Remove.");
            // e.printStackTrace();
        }
        removeGuest(guestId);
    }

    /**
     * Get all the guests' remotePaints
     */
    public Map<String, IRemotePaint> getGuestRemotePaints() {
        return guestRemotePaints;
    }

    /**
     * Add a visitor to the visitor list, when the visitor knock the door.
     */
    public void addVisitor(String userId, String ip, int registerPort) {
        visitors.put(userId, new User(userId, User.VISITOR, ip, registerPort, -1));
        try {
            Registry clientRegistry = LocateRegistry.getRegistry(ip, registerPort);
            // Registry clientRegistry = LimitedTimeRegistry.getLimitedTimeRegistry(ip, registerPort, 1000);
            IRemoteApp remoteVisitorApp = (IRemoteApp) clientRegistry.lookup("app");
            visitorRemoteApps.put(userId, remoteVisitorApp);
        } catch (Exception e) {
            System.err.println("Can not get the client registry.");
            // e.printStackTrace();
        }

        if (chatPanel != null)
            chatPanel.appendText("Visitor " + userId + " wants to join!\n");

        // refresh ui.
        if (clsp != null) {
            clsp.updateUserList();
        }
    }

    /**
     * Remove the visitor from the visitor list.
     */
    public void removeVisitor(String userId) {
        visitors.remove(userId);
        visitorRemoteApps.remove(userId);
        if (chatPanel != null)
            chatPanel.appendText("Visitor " + userId + " leaves!\n");
        // refresh ui.
        if (clsp != null) {
            clsp.updateUserList();
        }
    }

    public void kickVisitor(String userId) {
        try {
            IRemoteApp remoteApp = visitorRemoteApps.get(userId);
            remoteApp.askOut();
        } catch (Exception e) {
            System.err.println("Can not get the client registry.");
            // e.printStackTrace();
        }

        removeVisitor(userId);
    }

    /**
     * Get guest list.
     */
    public Map<String, User> getGuests() {
        return guests;
    }

    /**
     * Update all the guests.
     */
    public void setGuests(Map<String, User> guests) {
        this.guests = guests;
        // refresh ui.
        if (clsp != null) {
            clsp.updateUserList();
        }
    }

    /**
     * Get visitor list.
     */
    public Map<String, User> getVisitors() {
        return visitors;
    }

    /**
     * Get host.
     */
    public User getHost() {
        return host;
    }

    /**
     * Get the remotePaint of host
     */
    public IRemotePaint getHostRemotePaint() {
        return hostRemotePaint;
    }

    /**
     * Set the clientListScrollPanel.
     */
    public void setCLSP(ClientListScrollPanel clsp) {
        this.clsp = clsp;
    }

    /**
     * Get the identity of a given userId.
     */
    public int getIdentity(String userId) {
        if (host.getUserId().equals(userId)) {
            return User.HOST;
        } else if (guests.containsKey(userId)) {
            return User.GUEST;
        } else {
            return User.VISITOR;
        }
    }

    public IRemoteApp getHostRemoteApp() {
        return hostRemoteApp;
    }

    public void setHostRemoteApp(IRemoteApp hostRemoteApp) {
        this.hostRemoteApp = hostRemoteApp;
    }
}
