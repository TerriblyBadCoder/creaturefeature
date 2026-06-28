#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler4;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float GameTime;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord1;

out vec4 fragColor;
vec3 rgb2hsv(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}
void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
    vec2 Size = textureSize(Sampler0,0);
    vec3 color2 = rgb2hsv(color.rgb);
    float xEd = texCoord0.x;
    xEd-=fract(xEd*Size.x)/Size.x;
    float yEd = texCoord0.y;
    yEd-=fract(yEd*Size.y)/Size.y;
    if(length(texture(Sampler4,vec2(0.0,0.0)).rgb)<0.1){
        float fracted =yEd*3.14*100.0+xEd*3.14*50.0+sin(xEd*3.14*50+GameTime*600.0)/2.0;
        fracted-=fract(fracted*4.0f)/4.0f;
        color2.x=0.07+sin(fracted+GameTime*1200.0)/11.0;
        color2.y*=0.7;
        color.xyz=hsv2rgb(color2);

    }else{

        Size = textureSize(Sampler0,0);
        xEd = texCoord0.x;
        xEd-=fract(xEd*Size.x)/Size.x;
        yEd = texCoord0.y;
        yEd-=fract(yEd*Size.y)/Size.y;
        color2.y=0.0f;
        color.xyz=hsv2rgb(color2);
        color*=texture(Sampler4,vec2(xEd*100.0,yEd*100.0+sin(xEd*250.0*3.14+GameTime*200.0)/8.0+GameTime*100.0));
        color2 = rgb2hsv(color.rgb);
        color2.x*=1.0+sin(xEd*50.0*3.14+yEd*10.0*3.14+GameTime*600.0)/40.0;
        color.xyz=hsv2rgb(color2);
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
