#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float Revealness;
uniform vec3 Light0_Direction;
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

    vec3 magic = vec3(0.05711056f, 0.00083715f, 52.9829189f);
    ivec2 uvd = ivec2(gl_FragCoord.xy);
    vec2 uvd2 = vec2(uvd.x,uvd.y)/ScreenSize;

    //new
    float depth = texture(Sampler0,uvd2).x;
    //vec3 ndc = vec3(SCREEN_UV, depth) * 2.0 - 1.0;
    vec3 ndc = vec3(uvd2 * 2.0 - 1.0, depth);
    vec4 view = inverse(ProjMat) * vec4(ndc, 1.0);
    view.xyz /= view.w;
    float linear_depth = -view.z;

    float object_depth = gl_FragCoord.z;
    vec3 object_ndc = vec3(uvd2 * 2.0 - 1.0, object_depth);
    vec4 object_view = inverse(ProjMat) * vec4(object_ndc, 1.0);
    object_view.xyz /= object_view.w;
    float linear_object_depth = -object_view.z;
    vec4 vertexColorCopy = vertexColor;
    vertexColorCopy.a*=2.0;
    vec4 color = vec4(1.0,1.0,1.0,0.1f);
    color.a=min(1.0,color.a);
    if( abs(linear_depth-linear_object_depth)<0.2 ){
        color = vec4(1.0,1.0,1.0,1.0)*vertexColor;
    }else{
        discard;
    }

    if(lightMapColor.rgb!=vec3(0.0,0.0,0.0))
        color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
