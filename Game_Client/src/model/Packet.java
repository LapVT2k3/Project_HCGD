package model;

import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String header;
    private Object data;

    public Packet() {
    }

    public Packet(String header, Object data) {
        this.header = header;
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Object getContent() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
