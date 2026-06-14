package com.scale.modelModifier.mixins;

import com.scale.modelModifier.Main;
import com.scale.modelModifier.utils.TargetUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class TickAntibotMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler == null) return;

        for (PlayerEntity player : Main.w().getPlayers()) {
            if (networkHandler.getPlayerListEntry(player.getUuid()) != null) {
                TargetUtil.ENTITY_VALID_TICKS.putIfAbsent(player.getUuid(), 0);
                TargetUtil.ENTITY_VALID_TICKS.put(player.getUuid(), TargetUtil.ENTITY_VALID_TICKS.get(player.getUuid()) + 1);
            }
            else TargetUtil.ENTITY_VALID_TICKS.remove(player.getUuid());
        }
    }
}