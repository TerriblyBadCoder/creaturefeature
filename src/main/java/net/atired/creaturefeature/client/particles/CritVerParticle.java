package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class CritVerParticle extends TextureSheetParticle {
    float addToAngle = 0.0f;
    boolean bounced=false;
    SpriteSet spr;
    protected CritVerParticle(ClientLevel clientWorld, double d, double e, double f, SpriteSet spriteProvider, double xd, double yd, double zd) {
        super(clientWorld, d, e, f,xd,yd,zd);
        this.x = d;
        this.y = e;
        this.z = f;
        this.addToAngle = 0f;
        this.yd=yd/1.0f;
        if(this.yd<0){
            this.yd/=4;
        }
        this.rCol=1f;
        this.gCol=1f;
        this.bCol=1f;
        this.xd=xd/1.0f;
        this.zd=zd/1.0f;
        if(Math.abs(xd)+Math.abs(zd)<0.01f){
            this.bounced=true;
        }
        this.friction=0.95f;
        this.lifetime=10;
        if(this.bounced){
            this.gravity=-0.2f;
        }else{
            this.gravity=1.7f;
        }
        this.age=0;
        this.quadSize=0;
        this.spr=spriteProvider;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
    }

    @Override
    public void tick() {
        boolean wasongr = onGround;
        double oly = this.y;
//        if(this.onGround&&this.gravity>0){
//            this.gravity=-0.05f;
//            this.yd=0.1;
//        }
        super.tick();
//        if(this.onGround&&this.gravity>0){
//            this.gravity=-0.05f;
//            this.yd=0.1;
//        }
        oly = Math.abs(oly-this.y);
        if(onGround&&!bounced){
            onGround=false;
            yd=0.2+Math.random()/7.0f;
            xd*=1.4f;
            zd*=1.4f;
        }
        this.setSpriteFromAge(this.spr);
        this.oRoll = this.roll;
//        if(this.gravity>-0.5)
//            this.gravity-=0.05f;
        this.roll+= 2f*(float)oly;
        if(this.roll>6.28f){
            this.roll-=6.28f;
        }
        float aged=  (float)this.age/(float)this.lifetime;
        this.quadSize+=(1.0f-aged)/8.0f;
        alpha=Math.min((1.0f-aged)*4.0f,1.0f);
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if(this.bounced){
            super.render(buffer,renderInfo,partialTicks);
            return;
        }
        this.addToAngle=Mth.lerp(0.2f,this.addToAngle,1f);
        float angled = this.oRoll*(1.0f-partialTicks)+this.roll*partialTicks;
        this.quadSize*=this.addToAngle;
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().scale(this.addToAngle).rotationZYX(0,angled,-angled+3.14f/2f), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().scale(this.addToAngle).rotationZYX(0,angled+3.14f,angled-3.14f/2f), partialTicks);
        this.quadSize/=this.addToAngle;
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
            CritVerParticle flameparticle = new CritVerParticle(level, x, y, z,sprite, xSpeed, ySpeed, zSpeed);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
