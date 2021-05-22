package org.unimelb.whiteboard.standard;

public final class StateCode {
    // Central Server Operation
    // Room operation code
    public final static int GET_ROOM_LIST = 10;
    public final static int ADD_ROOM = 11;
    public final static int REMOVE_ROOM = 12;
    public final static int GET_ROOM_INFO = 13;

    // User operation
    public final static int ADD_USER = 20;
    public final static int REMOVE_USER = 21;

    // Operation success or not
    public final static int SUCCESS = 30;
    public final static int FAIL = 31;
    public final static int PASSWORD_WRONG = 33;

    // Connection Problem
    public final static int CONNECTION_SUCCESS = 40;
    public final static int CONNECTION_FAIL = 41;
    public final static int UNKNOWN_HOST = 403;
    public final static int COLLECTION_REFUSED = 403;
    public final static int IO_ERROR = 403;
    public final static int TIMEOUT = 400;
}
