package com.nutonmod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public record VoidResonanceRecipe(Ingredient input, Ingredient catalyst, ItemStack result, int time) implements Recipe<SingleStackRecipeInput> {
    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return this.input.test(input.item());
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registryLookup) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static final class Type implements RecipeType<VoidResonanceRecipe> {
        public static final String ID = "void_resonance";
        public static final Type INSTANCE = new Type();
    }

    public static final class Serializer implements RecipeSerializer<VoidResonanceRecipe> {
        public static final String ID = "void_resonance";
        public static final Serializer INSTANCE = new Serializer();

        private static final MapCodec<VoidResonanceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(VoidResonanceRecipe::input),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("catalyst").forGetter(VoidResonanceRecipe::catalyst),
                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(VoidResonanceRecipe::result),
                net.minecraft.util.dynamic.Codecs.POSITIVE_INT.optionalFieldOf("time", 200).forGetter(VoidResonanceRecipe::time)
        ).apply(instance, VoidResonanceRecipe::new));

        private static final PacketCodec<RegistryByteBuf, VoidResonanceRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, VoidResonanceRecipe::input,
                Ingredient.PACKET_CODEC, VoidResonanceRecipe::catalyst,
                ItemStack.PACKET_CODEC, VoidResonanceRecipe::result,
                PacketCodecs.INTEGER, VoidResonanceRecipe::time,
                VoidResonanceRecipe::new
        );

        @Override
        public MapCodec<VoidResonanceRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, VoidResonanceRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
