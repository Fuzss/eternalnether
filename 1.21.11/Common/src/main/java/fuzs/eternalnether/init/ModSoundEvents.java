package fuzs.eternalnether.init;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public final class ModSoundEvents {
    public static final Holder.Reference<SoundEvent> WITHER_WALTZ = register("wither_waltz");
    public static final Holder.Reference<SoundEvent> ITEM_SWORD_BLOCK_SOUND_EVENT = register("item.sword.block");
    public static final Holder.Reference<SoundEvent> WEX_CHARGE = register("entity.wex.charge");
    public static final Holder.Reference<SoundEvent> WEX_DEATH = register("entity.wex.death");
    public static final Holder.Reference<SoundEvent> WEX_HURT = register("entity.wex.hurt");
    public static final Holder.Reference<SoundEvent> WEX_AMBIENT = register("entity.wex.ambient");
    public static final Holder.Reference<SoundEvent> WARPED_ENDERMAN_DEATH = register("entity.warped_enderman.death");
    public static final Holder.Reference<SoundEvent> WARPED_ENDERMAN_HURT = register("entity.warped_enderman.hurt");
    public static final Holder.Reference<SoundEvent> WARPED_ENDERMAN_AMBIENT = register("entity.warped_enderman.ambient");
    public static final Holder.Reference<SoundEvent> WARPED_ENDERMAN_TELEPORT = register(
            "entity.warped_enderman.teleport");
    public static final Holder.Reference<SoundEvent> WARPED_ENDERMAN_SCREAM = register("entity.warped_enderman.scream");
    public static final Holder.Reference<SoundEvent> WARPED_ENDERMAN_STARE = register("entity.warped_enderman.stare");

    private static Holder.Reference<SoundEvent> register(String path) {
        return ModRegistry.REGISTRIES.registerSoundEvent(path);
    }

    public static void boostrap() {
        // NO-OP
    }
}