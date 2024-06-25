package org.dimdev.dimdoors.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.dimdev.dimdoors.entity.PatrolNodeEntity;

public class PatrolNodeRenderer extends EntityRenderer<PatrolNodeEntity> {

    public PatrolNodeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(PatrolNodeEntity entity) {
        return null;
    }

    @Override
    public boolean shouldRender(PatrolNodeEntity entity, Frustum frustum, double x, double y, double z) { return false; }
}
