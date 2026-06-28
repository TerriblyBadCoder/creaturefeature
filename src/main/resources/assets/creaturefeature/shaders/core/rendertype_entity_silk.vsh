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
uniform float GameTime;
uniform vec3 Light0_Direction;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec4 lightMapColor;
out vec4 overlayColor;
out vec2 texCoord0;

void main() {
    vec3 positioner = Position;
    vertexDistance = fog_distance(Position, FogShape);
    vertexColor = Color;
    if(OffYPosition>-300.0||OffYPosition<-301.0){
        vertexColor.a*=min(1.0,(1.9-clamp(Position.y-OffYPosition,0.0,1.9))*1.5);
        lightMapColor = vec4(1,1,1,1);
    }
    else{
        lightMapColor=texelFetch(Sampler2, UV2 / 16, 0);
    }

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);


    overlayColor = texelFetch(Sampler1, UV1, 0);
    texCoord0 = UV0;
}
