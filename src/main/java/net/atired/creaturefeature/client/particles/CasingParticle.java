package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CasingParticle extends TextureSheetParticle {
    float addToAngle = 0.0f;
    boolean bounced=false;
    SpriteSet spr;
    protected CasingParticle(ClientLevel clientWorld, double d, double e, double f, SpriteSet spriteProvider, double xd, double yd, double zd) {
        super(clientWorld, d, e, f,xd,yd,zd);
        this.x = d;
        this.y = e;
        this.z = f;
        this.addToAngle = (float)Math.random()*10.0f;
        this.yd=yd/1.0f;
        if(this.yd<0){
            this.yd/=4;
        }
        this.xd=xd/1.0f;
        this.zd=zd/1.0f;
        this.friction=0.95f;
        this.lifetime=400;
        this.gravity=1.0f;
        this.quadSize=(0.4f+(float)Math.random()/2f)*0.3f;
        this.spr=spriteProvider;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
    }

    @Override
    public void tick() {
        boolean wasongr = onGround;
        double oly = this.y;
        super.tick();
        oly = Math.abs(oly-this.y);
        if(onGround&&!bounced){
            bounced=true;
            onGround=false;
            yd=0.2+Math.random()/7.0f;
            xd*=1.4f;
            zd*=1.4f;
        }
        this.setSpriteFromAge(this.spr);
        this.oRoll = this.roll;
        this.roll+= 3f*(float)oly;
        if(this.roll>6.28f){
            this.roll-=6.28f;
        }
        if(this.age>380){
            this.quadSize*=0.9f;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float angled = this.oRoll*(1.0f-partialTicks)+this.roll*partialTicks;
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(angled,angled,-angled+3.14f/2f), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(angled,angled+3.14f,angled-3.14f/2f), partialTicks);
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
            CasingParticle flameparticle = new CasingParticle(level, x, y, z,sprite, xSpeed, ySpeed, zSpeed);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
