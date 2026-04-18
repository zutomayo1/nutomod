package com.nutonmod.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record StabilizerBeaconData(BlockPos pos) implements BlockPosPayload {
    public static final PacketCodec<RegistryByteBuf, StabilizerBeaconData> CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, StabilizerBeaconData::pos, StabilizerBeaconData::new);
}
