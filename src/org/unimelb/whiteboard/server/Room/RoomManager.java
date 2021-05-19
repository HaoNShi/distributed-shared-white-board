package org.unimelb.whiteboard.server.Room;

import org.unimelb.whiteboard.client.StateCode.StateCode;

import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    private final Map<Integer, Room> rooms;
    private final int updateTimes;
    private int nextID;

    public RoomManager() {
        rooms = new HashMap<>();
        nextID = 0;
        updateTimes = 0;
    }

    public synchronized int addRoom(String ipAddress, int port, String hostName, String roomName, String password) {
        Room room = new Room(nextID, ipAddress, port, hostName, roomName, password);
        rooms.put(nextID, room);
        nextID += 1;
        return nextID - 1;
    }

    public synchronized int getRoomNum() {
        return rooms.size();
    }

    public synchronized Map<Integer, String> getRoomList() {
        Map<Integer, String> reqRooms = new HashMap<>();
        for (Map.Entry<Integer, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();
            reqRooms.put(entry.getKey(), room.getRoomName() + ' ' + room.getHostName());
        }
        return reqRooms;
    }

    public synchronized int removeRoom(int roomId) {
        if (rooms.containsKey(roomId)) {
            rooms.remove(roomId);
            return StateCode.SUCCESS;
        } else {
            return StateCode.FAIL;
        }
    }

    public synchronized void removeRoom(String hostId) {
        for (Map.Entry<Integer, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();
            if (room.getHostName().equals(hostId)) {
                rooms.remove(entry.getKey());
                break;
            }
        }
    }

    public synchronized boolean checkRoomPassword(int roomId, String password) {
        if (rooms.containsKey(roomId)) {
            return password.equals(rooms.get(roomId).getPassword());
        } else {
            return false;
        }
    }

    public synchronized Room getRoomInfo(int roomID) {
        return rooms.get(roomID);
    }

    public synchronized int getUpdateTimes() {
        return updateTimes;
    }
}
