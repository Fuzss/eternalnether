package fuzs.eternalnether.world.entity.monster;

import com.google.common.collect.ImmutableMap;
import fuzs.eternalnether.init.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

import java.util.Map;

public class Wex extends Vex {
    private static final Map<SoundEvent, SoundEvent> SOUND_EVENTS = ImmutableMap.of(SoundEvents.VEX_AMBIENT,
            ModSoundEvents.WEX_AMBIENT.value(),
            SoundEvents.VEX_DEATH,
            ModSoundEvents.WEX_DEATH.value(),
            SoundEvents.VEX_HURT,
            ModSoundEvents.WEX_HURT.value(),
            SoundEvents.VEX_CHARGE,
            ModSoundEvents.WEX_CHARGE.value());

    public Wex(EntityType<? extends Vex> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficultyInstance) {
        // NO-OP
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void playSound(SoundEvent event, float volume, float pitch) {
        super.playSound(SOUND_EVENTS.getOrDefault(event, event), volume, pitch);
    }
}
