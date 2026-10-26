package com.replaymod.render.blend.mixin;

import java.util.List;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({RenderGlobal.class})
public interface WorldRendererAccessor {
   @Accessor
   List<ContainerLocalRenderInformationAccessor> getRenderInfos();
}
