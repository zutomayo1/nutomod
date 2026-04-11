package com.nutonmod.sound;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

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
    public static final SoundEvent SINGULARITY_SUMMON = register("singularity_summon");
    public static final SoundEvent SINGULARITY_PHASE_SHIFT = register("singularity_phase_shift");
    public static final SoundEvent SINGULARITY_BARRAGE = register("singularity_barrage");
    public static final SoundEvent SINGULARITY_GRAVITY_WELL = register("singularity_gravity_well");
    public static final SoundEvent SINGULARITY_BLACK_HOLE = register("singularity_black_hole");
    public static final SoundEvent SINGULARITY_RAMPAGE = register("singularity_rampage");
    public static final SoundEvent SINGULARITY_NOVA = register("singularity_nova");
    public static final SoundEvent SINGULARITY_DEATH = register("singularity_death");
    public static final SoundEvent VOID_CALAMITY_SHOT = register("void_calamity_shot");
    public static final SoundEvent VOID_CALAMITY_ANNIHILATION = register("void_calamity_annihilation");
    public static final SoundEvent VOID_CALAMITY_CHARGE_FULL = register("void_calamity_charge_full");
    public static final SoundEvent STORM_SCEPTER_CAST = register("storm_scepter_cast");
    public static final SoundEvent STORM_SCEPTER_EYE = register("storm_scepter_eye");
    public static final SoundEvent STORM_SCEPTER_CHARGE = register("storm_scepter_charge");
    public static final SoundEvent STORM_SCEPTER_ARC = register("storm_scepter_arc");
    public static final SoundEvent STORM_SCEPTER_CHAIN_IMPACT = register("storm_scepter_chain_impact");
    public static final SoundEvent STORM_SCEPTER_EYE_PULSE = register("storm_scepter_eye_pulse");
    public static final SoundEvent THUNDER_SPEAR_THROW = register("thunder_spear_throw");
    public static final SoundEvent THUNDER_SPEAR_RETURN = register("thunder_spear_return");
    public static final SoundEvent THUNDER_RIPPER_SWING = register("thunder_ripper_swing");
    public static final SoundEvent THUNDER_RIPPER_CHAIN = register("thunder_ripper_chain");
    public static final SoundEvent THUNDER_RIPPER_DASH = register("thunder_ripper_dash");
    public static final SoundEvent THUNDER_RIPPER_TRAIL = register("thunder_ripper_trail");
    public static final SoundEvent THUNDER_RIPPER_IMPACT = register("thunder_ripper_impact");

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
