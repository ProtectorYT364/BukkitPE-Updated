package net.BukkitPE.raknet.protocol.packet;

import net.BukkitPE.raknet.RakNet;
import net.BukkitPE.raknet.protocol.Packet;

/**

 * BukkitPE Project
 */
public class OPEN_CONNECTION_REQUEST_1 extends Packet {
    public static final byte ID = (byte) 0x05;

    @Override
    public byte getID() {
        return ID;
    }

    public byte protocol = RakNet.PROTOCOL;
    public short mtuSize;

    @Override
    public void encode() {
        super.encode();
        this.put(RakNet.MAGIC);
        this.putByte(this.protocol);
        this.put(new byte[this.mtuSize - 18]);
    }

    @Override
    public void decode() {
        super.decode();
        this.offset += 16; //skip magic bytes
        this.protocol = this.getByte();
        this.mtuSize = (short) (this.get().length + 18);
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new OPEN_CONNECTION_REQUEST_1();
        }

    }

}
