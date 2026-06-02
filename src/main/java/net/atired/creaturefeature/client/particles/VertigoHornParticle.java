package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class VertigoHornParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int rotoff=0;
    protected VertigoHornParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=20;
        this.rotoff=(int)(Math.random()*300*3.14f);
        this.gravity=-0.0f;
        this.yd=0.0f;
        this.xd=0.0f;
        this.zd=0.0f;
        this.quadSize*=5.0f;
        this.spriteSet=sprite;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
    }

    @Override
    public void tick() {
        this.oRoll=roll;
        float aged=  (float)this.age/(float)this.lifetime;
        this.quadSize+=aged/4.0f;

        this.roll+=(1.0f-aged)*0.2f;
        alpha=Math.min((1.0f-aged)*4.0f,1.0f);
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float angle = this.oRoll*(1.0f-partialTicks)+this.roll*partialTicks;
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(3.14f,angle,3.14f/2.0f), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0.0f,-angle,3.14f/2.0f), partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            VertigoHornParticle flameparticle = new VertigoHornParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
