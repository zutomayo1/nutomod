package com.nutonmod.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record VoidResonanceBoxData(BlockPos pos) implements BlockPosPayload {
    public static final PacketCodec<RegistryByteBuf, VoidResonanceBoxData> CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, VoidResonanceBoxData::pos, VoidResonanceBoxData::new);
}
