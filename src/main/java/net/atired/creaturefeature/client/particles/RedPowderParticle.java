package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class RedPowderParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    protected RedPowderParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=16;
        this.yd=0.0f;
        this.xd=xSpeed*(0.8f+Math.random()/5.0f)*0.1f;
        this.zd=zSpeed*(0.8f+Math.random()/5.0f)*0.1f;
        this.gravity=0;
        this.quadSize=0.2f;
        this.roll=(float)Math.random()*3.14f*4.0f;
        this.oRoll=this.roll;
        this.spriteSet=sprite;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public void tick() {
        this.quadSize=Mth.lerp(0.5f,this.quadSize,(1.0f+Mth.sin(roll)/4.0f)*0.5f);
        this.gravity= -Mth.cos(this.age/8.0f*3.14f)/1.0f;
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
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
            RedPowderParticle flameparticle = new RedPowderParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
