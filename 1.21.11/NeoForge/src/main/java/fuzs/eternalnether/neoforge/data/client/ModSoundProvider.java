package fuzs.eternalnether.neoforge.data.client;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.init.ModSoundEvents;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.neoforge.api.client.data.v2.AbstractSoundProvider;

public class ModSoundProvider extends AbstractSoundProvider {

    public ModSoundProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addSounds() {
        this.addRecord(ModSoundEvents.WITHER_WALTZ);
        this.add(ModSoundEvents.ITEM_SWORD_BLOCK_SOUND_EVENT.value(),
                sound(EternalNether.id("item/sword/block1")).volume(0.8),
                sound(EternalNether.id("item/sword/block2")).volume(0.8));
        this.add(ModSoundEvents.WARPED_ENDERMAN_AMBIENT.value(),
                EternalNether.id("entity/warped_enderman/idle1"),
                EternalNether.id("entity/warped_enderman/idle2"),
                EternalNether.id("entity/warped_enderman/idle3"),
                EternalNether.id("entity/warped_enderman/idle4"),
                EternalNether.id("entity/warped_enderman/idle5"));
        this.add(ModSoundEvents.WARPED_ENDERMAN_DEATH.value(), EternalNether.id("entity/warped_enderman/death"));
        this.add(ModSoundEvents.WARPED_ENDERMAN_HURT.value(),
                EternalNether.id("entity/warped_enderman/hit1"),
                EternalNether.id("entity/warped_enderman/hit2"),
                EternalNether.id("entity/warped_enderman/hit3"),
                EternalNether.id("entity/warped_enderman/hit4"));
        this.add(ModSoundEvents.WARPED_ENDERMAN_SCREAM.value(),
                EternalNether.id("entity/warped_enderman/scream1"),
                EternalNether.id("entity/warped_enderman/scream2"),
                EternalNether.id("entity/warped_enderman/scream3"),
                EternalNether.id("entity/warped_enderman/scream4"));
        this.add(ModSoundEvents.WARPED_ENDERMAN_STARE.value(), EternalNether.id("entity/warped_enderman/stare"));
        this.add(ModSoundEvents.WARPED_ENDERMAN_TELEPORT.value(),
                EternalNether.id("entity/warped_enderman/portal1"),
                EternalNether.id("entity/warped_enderman/portal2"));
        this.add(ModSoundEvents.WEX_AMBIENT.value(),
                EternalNether.id("entity/wex/idle1"),
                EternalNether.id("entity/wex/idle2"),
                EternalNether.id("entity/wex/idle3"),
                EternalNether.id("entity/wex/idle4"));
        this.add(ModSoundEvents.WEX_CHARGE.value(),
                EternalNether.id("entity/wex/charge1"),
                EternalNether.id("entity/wex/charge2"),
                EternalNether.id("entity/wex/charge3"));
        this.add(ModSoundEvents.WEX_DEATH.value(),
                EternalNether.id("entity/wex/death1"),
                EternalNether.id("entity/wex/death2"));
        this.add(ModSoundEvents.WEX_HURT.value(),
                EternalNether.id("entity/wex/hurt1"),
                EternalNether.id("entity/wex/hurt2"));
    }
}
