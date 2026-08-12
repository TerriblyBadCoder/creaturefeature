package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class GlassParticle extends TextureSheetParticle {
    float addToAngle = 0.0f;
    boolean bounced=false;
    SpriteSet spr;
    protected GlassParticle(ClientLevel clientWorld, double d, double e, double f, SpriteSet spriteProvider, double xd, double yd, double zd) {
        super(clientWorld, d, e, f,xd,yd,zd);
        this.x = d;
        this.y = e;
        this.z = f;
        this.addToAngle = 0f;
        this.yd=yd/1.0f;
        if(this.yd<0){
            this.yd/=4;
        }
        this.rCol=0.5f;
        this.gCol=0.5f;
        this.bCol=0.5f;
        this.xd=xd/1.0f;
        this.zd=zd/1.0f;
        int aged =  Math.clamp(54-(int)(new Vec3(xd,yd,zd).length()*60f),0,50);
        this.friction=0.95f;
        this.lifetime=100;
        this.gravity=1.0f-aged/75f;
        this.age=aged;
        this.quadSize=(0.4f+(float)Math.random()/2f)*0.4f;
        this.spr=spriteProvider;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
    }

    @Override
    public void tick() {
        boolean wasongr = onGround;
        double oly = this.y;

        if(this.gravity<0){
            this.rCol= Mth.lerp(0.1f,this.rCol,1f);
            this.gCol= Mth.lerp(0.1f,this.gCol,0.2f);
            this.bCol= Mth.lerp(0.1f,this.bCol,0.2f);
        }
        if(this.onGround&&this.gravity>0){
            this.gravity=-0.05f;
            this.yd=0.1;
        }
        super.tick();
        if(this.onGround&&this.gravity>0){
            this.gravity=-0.05f;
            this.yd=0.1;
        }
        oly = Math.abs(oly-this.y);
//        if(onGround&&!bounced){
//            bounced=true;
//            onGround=false;
//            yd=0.2+Math.random()/7.0f;
//            xd*=1.4f;
//            zd*=1.4f;
//        }
        this.setSpriteFromAge(this.spr);
        this.oRoll = this.roll;
        if(this.gravity>-0.5)
            this.gravity-=0.05f;
        this.roll+= 2f*(float)oly;
        if(this.roll>6.28f){
            this.roll-=6.28f;
        }
        if(this.age>80){
            this.quadSize*=0.9f;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
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
            GlassParticle flameparticle = new GlassParticle(level, x, y, z,sprite, xSpeed, ySpeed, zSpeed);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
