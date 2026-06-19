#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float Revealness;
uniform float Time;
uniform vec4 FogColor;
uniform vec2 ScreenSize;
uniform mat4 ProjMat;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec2 fracCoord = gl_FragCoord.xy/64.0/2.0*vec2(1,-1);
    fracCoord.x-=fract(fracCoord.x*32.0)/32.0;
    fracCoord.y-=fract(fracCoord.y*32.0)/32.0;
    vec4 color = texture(Sampler0, fracCoord+vec2(-Time*1.4,sin(fracCoord.x*3.14/2.0+Time*2.0)/5.0))*0.67+texture(Sampler0, fracCoord*vec2(-1,1)+vec2(-Time*1.6,0.5+cos(fracCoord.x*3.14/2.0+Time*3.0)/5.0))*0.33;

    vec3 magic = vec3(0.05711056f, 0.00083715f, 52.9829189f);
    ivec2 uvd = ivec2(gl_FragCoord.xy);
    vec2 uvd2 = vec2(uvd.x,uvd.y)/ScreenSize;

    //new

    float object_depth = gl_FragCoord.z;
    vec3 object_ndc = vec3(uvd2 * 2.0 - 1.0, object_depth);
    vec4 object_view = inverse(ProjMat) * vec4(object_ndc, 1.0);
    object_view.xyz /= object_view.w;
    float linear_object_depth = -object_view.z/4.0;

    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    if(lightMapColor.rgb!=vec3(0.0,0.0,0.0))
        color *= lightMapColor;

    fracCoord=texCoord0;
    fracCoord.x-=fract(fracCoord.x*32.0)/32.0;
    fracCoord.y-=fract(fracCoord.y*32.0)/32.0;
    float len = length(fracCoord-vec2(0.5,0.5))*1.0;
    len-=fract(len*32.0)/32.0;
    color.rgb*=min(1.0,len*0.5+0.6);
    if(color.a<0.33){discard;}
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
