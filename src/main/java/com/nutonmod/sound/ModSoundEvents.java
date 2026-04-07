package com.nutonmod.sound;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.http.impl.cookie.PublicSuffixDomainFilter;

public class ModSoundEvents {
    public static final SoundEvent PROSPECTOR_FOUND_ORE = register("prospector_found_ore");
    public static final SoundEvent ENERGY_BLOCK_BREAK = register("energy_block_break");
    public static final SoundEvent ENERGY_BLOCK_PLACE = register("energy_block_place");
    public static final SoundEvent ENERGY_BLOCK_HIT = register("energy_block_hit");
    public static final SoundEvent ENERGY_BLOCK_FALL = register("energy_block_fall");
    public static final SoundEvent ENERGY_BLOCK_STEP = register("energy_block_step");
    public static final SoundEvent STORM_ARBITER_SUMMON = register("storm_arbiter_summon");
    public static final SoundEvent STORM_ARBITER_LIGHTNING_CHAIN = register("storm_arbiter_lightning_chain");
    public static final SoundEvent STORM_ARBITER_SPEAR_THROW = register("storm_arbiter_spear_throw");
    public static final SoundEvent STORM_ARBITER_STORM_CLOUD = register("storm_arbiter_storm_cloud");
    public static final SoundEvent STORM_ARBITER_BLADE_STORM = register("storm_arbiter_blade_storm");
    public static final SoundEvent STORM_ARBITER_THUNDERFALL = register("storm_arbiter_thunderfall");
    public static final SoundEvent STORM_ARBITER_FINAL_JUDGEMENT = register("storm_arbiter_final_judgement");
    public static final SoundEvent STORM_ARBITER_DEATH = register("storm_arbiter_death");

    public static final BlockSoundGroup ENERGY_BLOCK_SOUND_GROUP = new BlockSoundGroup(1.0f, 1.0f,
            ENERGY_BLOCK_BREAK,
            ENERGY_BLOCK_STEP,
            ENERGY_BLOCK_PLACE,
            ENERGY_BLOCK_HIT,
            ENERGY_BLOCK_FALL
    );
    public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_TEST= registerReference("music_disc_test");
    private static SoundEvent register(String name){
        Identifier id = Identifier.of(NutonMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    private static RegistryEntry.Reference<SoundEvent> registerReference(String name){
        Identifier id = Identifier.of(NutonMod.MOD_ID, name);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSoundEvents(){
        NutonMod.LOGGER.info("Registering Sound Events for " + NutonMod.MOD_ID);
    }

}
