package controller;

import model.Packet;

/**
 *
 * @author ADMIN
 */
public interface PacketListener {
    void onPacketReceived(Packet packet);
}
