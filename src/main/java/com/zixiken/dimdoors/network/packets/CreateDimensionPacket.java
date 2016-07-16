package com.zixiken.dimdoors.network.packets;

import com.zixiken.dimdoors.watcher.ClientDimData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;

public class CreateDimensionPacket implements IMessage {
    private ClientDimData dimensionData = null;

    public CreateDimensionPacket() {}
    public CreateDimensionPacket(ClientDimData data) {
        this.dimensionData = data;
    }

    public ClientDimData getDimensionData() { return dimensionData; }

    @Override
    public void fromBytes(ByteBuf in) {
        try {
            dimensionData = ClientDimData.read(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf out) {
        if (dimensionData != null) {
            try {
                dimensionData.write(out);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
