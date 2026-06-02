#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float Revealness;
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

    vec4 color = texture(Sampler0, texCoord0);

    vec3 magic = vec3(0.05711056f, 0.00083715f, 52.9829189f);
    ivec2 uvd = ivec2(gl_FragCoord.xy);
    vec2 uvd2 = vec2(uvd.x,uvd.y)/ScreenSize;

    //new

    float object_depth = gl_FragCoord.z;
    vec3 object_ndc = vec3(uvd2 * 2.0 - 1.0, object_depth);
    vec4 object_view = inverse(ProjMat) * vec4(object_ndc, 1.0);
    object_view.xyz /= object_view.w;
    float rev = min(1.0,Revealness);
    float linear_object_depth = -object_view.z/4.0*(1.0-rev);
    color *= vertexColor * ColorModulator;
    color.a*=clamp(2.0-linear_object_depth,0.0,1.0);
    uvd.x-=uvd.x%4;
    uvd.y-=uvd.y%4;

    float fracted = fract(magic.z * fract(dot(uvd.xy, magic.xy)));
    if(color.a < fracted||color.a<0.02){
        discard;
    }
    else{
        color.a = 1.0;
    }
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    if(lightMapColor.rgb!=vec3(0.0,0.0,0.0))
        color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
