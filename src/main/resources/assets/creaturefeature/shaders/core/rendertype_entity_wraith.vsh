#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;
uniform float OffYPosition;
uniform float Flipped;
uniform float GameTime;
uniform float Fade;
uniform vec3 Light0_Direction;
uniform vec3 TypeMult;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec4 lightMapColor;
out vec4 overlayColor;
out vec2 texCoord0;

void main() {
    vec3 positioner = Position;
    vertexDistance = fog_distance(positioner, FogShape);
    vertexColor = Color;
    vertexColor = minecraft_mix_light(Light0_Direction, Light1_Direction, vec3(0,0,1), Color);
    if(OffYPosition>-300.0||OffYPosition<-300.7){
        float clamped = clamp((positioner.y-OffYPosition)/1.5,0.0,1.0);
        if(Flipped>0.5){
            clamped=1.0-clamped;
        }
        vertexColor.a*=clamped*Fade;
        float verPow =  pow(max((vertexColor.a-0.2)/0.8,0.0),2.0);
        vertexColor.rgb=vertexColor.rgb*verPow+vec3(42.0/255.0,60.0/255.0,132.0/255.0)*(1.0-verPow);
        lightMapColor = vec4(1,1,1,1)*verPow+texelFetch(Sampler2, UV2 / 16, 0)*(1.0f-verPow);
    }else{
        lightMapColor=texelFetch(Sampler2, UV2 / 16, 0);
    }
    gl_Position = ProjMat * ModelViewMat * vec4(positioner, 1.0);


    overlayColor = texelFetch(Sampler1, UV1, 0);
    texCoord0 = UV0;
}
