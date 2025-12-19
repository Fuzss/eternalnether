package fuzs.eternalnether.fabric.client;

import fuzs.eternalnether.EternalNether;
import fuzs.eternalnether.client.EternalNetherClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EternalNetherFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EternalNether.MOD_ID, EternalNetherClient::new);
    }
}
