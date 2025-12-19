package fuzs.eternalnether.world.entity.monster;

import fuzs.eternalnether.EternalNether;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public interface ShieldedMob {
    Identifier SPEED_MODIFIER_BLOCKING_ID = EternalNether.id("blocking");
    AttributeModifier SPEED_MODIFIER_BLOCKING = new AttributeModifier(SPEED_MODIFIER_BLOCKING_ID,
            -0.10,
            AttributeModifier.Operation.ADD_VALUE);

    boolean isUsingShield();

    void setUsingShield(boolean isShielded);

    boolean isShieldDisabled();

    void startUsingShield();

    void stopUsingShield();

    static <T extends Mob & ShieldedMob> void startUsingShield(T mob) {
        if (!mob.isUsingShield() && !mob.isShieldDisabled()) {
            for (InteractionHand interactionHand : InteractionHand.values()) {
                if (mob.getItemInHand(interactionHand).has(DataComponents.BLOCKS_ATTACKS)) {
                    mob.startUsingItem(interactionHand);
                    mob.setUsingShield(true);
                    AttributeInstance attributeInstance = mob.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (attributeInstance != null && !attributeInstance.hasModifier(SPEED_MODIFIER_BLOCKING_ID)) {
                        attributeInstance.addTransientModifier(SPEED_MODIFIER_BLOCKING);
                    }
                }
            }
        }
    }

    static <T extends Mob & ShieldedMob> void stopUsingShield(T mob) {
        if (mob.isUsingShield()) {
            for (InteractionHand interactionHand : InteractionHand.values()) {
                if (mob.getItemInHand(interactionHand).has(DataComponents.BLOCKS_ATTACKS)) {
                    mob.stopUsingItem();
                    mob.setUsingShield(false);
                    AttributeInstance attributeinstance = mob.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (attributeinstance != null) {
                        attributeinstance.removeModifier(SPEED_MODIFIER_BLOCKING);
                    }
                }
            }
        }
    }
}
