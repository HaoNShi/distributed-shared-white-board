package org.unimelb.whiteboard.client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.unimelb.whiteboard.standard.StateCode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * Use to get out of some wrong ip address after trying for some times.
 */
public class ExecuteThread extends Thread {
    private final String address;
    private final int port;
    private final JSONObject reqJSON;
    private int connectState;
    private Socket socket;
    private JSONObject resJSON;

    public ExecuteThread(String address, int port, JSONObject reqJSON) {
        this.address = address;
        this.port = port;
        this.connectState = StateCode.CONNECTION_FAIL;
        this.reqJSON = reqJSON;
        this.resJSON = new JSONObject();
        socket = null;
    }

    public int getConnectState() {
        return connectState;
    }

    public JSONObject getResJSON() {
        if (connectState != StateCode.CONNECTION_SUCCESS)
            resJSON.put("state", connectState);
        return resJSON;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();  // has problem
            socket.connect(new InetSocketAddress(address, port), 2000);
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(reqJSON.toJSONString());
            writer.flush();
            String res = reader.readUTF();
            resJSON = JSON.parseObject(res);
            reader.close();
            writer.close();
            connectState = StateCode.CONNECTION_SUCCESS;
            System.out.println("Connect to server successfully.");
        } catch (UnknownHostException e) {
            connectState = StateCode.UNKNOWN_HOST;
            System.err.println("Error: Unknown host!");
        } catch (ConnectException e) {
            connectState = StateCode.COLLECTION_REFUSED;
            System.err.println("Error: Collection refused!");
        } catch (SocketTimeoutException e) {
            connectState = StateCode.TIMEOUT;
            System.err.println("Error: Timeout!");
        } catch (IOException e) {
            connectState = StateCode.IO_ERROR;
            System.err.println("Error: I/O Error!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

