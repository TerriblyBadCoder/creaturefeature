package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CritTextParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int rotoff=0;
    protected CritTextParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=20+(int)(Math.random()*5);
        this.rotoff=(int)(Math.random()*300*3.14f);
        this.gravity=-0.3f;
        this.quadSize*=0.1f;
        this.yd=0;
        this.spriteSet=sprite;
    }

    @Override
    public void tick() {
        this.oRoll=roll;
        this.roll=(float)Mth.cos((age+rotoff)*0.4f)*0.4f;
        xd= Mth.sin(age/2.0f+rotoff)/32.0f;
        zd= Mth.sin(age/2.0f+rotoff)/32.0f;
        alpha=Math.clamp((0.95f-(float)this.age/this.lifetime)*2f,0f,1f);
        if(this.quadSize<0.5f){
            this.quadSize+=.07f;
        }
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        boolean remov=false;
        if(this.quadSize<0.5){
            remov=true;
            this.quadSize+=partialTicks*.07f;
        }
        super.render(buffer, renderInfo, partialTicks);
        if(remov){
            this.quadSize-=partialTicks*.07f;
        }
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
            CritTextParticle flameparticle = new CritTextParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
