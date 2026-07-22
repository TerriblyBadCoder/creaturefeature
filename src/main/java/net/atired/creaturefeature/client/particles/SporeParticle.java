package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SporeParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int delay = 0;
    protected SporeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=40;
        this.yd=0.0f;
        this.xd*=0.3f;
        this.zd*=0.3f;
        this.gravity=-0.1f;
        this.quadSize*=1.2f+(float)Math.random()*1.0f;
        this.roll=(float)Math.random()*3.14f*4.0f;
        this.spriteSet=sprite;
    }

    @Override
    public void tick() {
        this.delay-=1;
        if(this.delay>0)
            return;
        this.oRoll=roll;
        this.roll+=(1.0f-(float)this.age/this.lifetime)/7.0f;
        alpha=Math.min((1.0f-(float)((float)this.age/(float)this.lifetime))*4.0f,1.0f);
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if(this.delay>0)
            return;
        super.render(buffer, renderInfo, partialTicks);
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
            SporeParticle flameparticle = new SporeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
