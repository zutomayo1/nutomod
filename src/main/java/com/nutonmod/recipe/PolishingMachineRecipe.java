package com.nutonmod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class PolishingMachineRecipe implements Recipe<SingleStackRecipeInput> {
    public static final int DEFAULT_TIME = 72;
    private final ItemStack output;
    private final Ingredient ingredient;
    private final int time;

    public PolishingMachineRecipe(Ingredient ingredient, ItemStack output, int time) {
        this.output = output;
        this.ingredient = ingredient;
        this.time = Math.max(1, time);
    }


    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.add(this.ingredient);
        return ingredients;
    }
    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return this.ingredient.test(input.item());
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.output;
    }

    public Ingredient ingredient() {
        return this.ingredient;
    }

    public int time() {
        return this.time;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<PolishingMachineRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "polishing_machine";
    }
    public static class Serializer implements RecipeSerializer<PolishingMachineRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "polishing_machine";
        public static final MapCodec<PolishingMachineRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(PolishingMachineRecipe::ingredient),
                ItemStack.VALIDATED_CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                net.minecraft.util.dynamic.Codecs.POSITIVE_INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(PolishingMachineRecipe::time)
        ).apply(instance, PolishingMachineRecipe::new));
        public static final PacketCodec<RegistryByteBuf, PolishingMachineRecipe> PACKET_CODEC = PacketCodec.ofStatic(
            Serializer::write, Serializer::read);

        private static void write(RegistryByteBuf registryByteBuf, PolishingMachineRecipe polishingMachineRecipe) {
            Ingredient.PACKET_CODEC.encode(registryByteBuf, polishingMachineRecipe.ingredient());
            ItemStack.PACKET_CODEC.encode(registryByteBuf, polishingMachineRecipe.getResult(null));
            registryByteBuf.writeVarInt(polishingMachineRecipe.time());
        }

        private static PolishingMachineRecipe read(RegistryByteBuf registryByteBuf) {
            Ingredient ingredient = Ingredient.PACKET_CODEC.decode(registryByteBuf);
            ItemStack output = ItemStack.PACKET_CODEC.decode(registryByteBuf);
            int time = registryByteBuf.readVarInt();
            return new PolishingMachineRecipe(ingredient, output, time);
        }

        public MapCodec<PolishingMachineRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, PolishingMachineRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
