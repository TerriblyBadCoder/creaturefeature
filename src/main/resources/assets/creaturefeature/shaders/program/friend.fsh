#version 150
uniform sampler2D DiffuseSampler;
uniform sampler2D TrueDepthSampler;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D FriendSampler;
uniform sampler2D FriendDepthSampler;
in vec2 texCoord;
in vec2 oneTexel;
uniform vec2 InSize;
uniform float GameTime;
uniform float _FOV;

out vec4 fragColor;
const float near = 0.01;
const float far = 100.0;
float LinearizeDepth(float depth)
{
    float z = depth * 2.0f - 1.0f;
    return (near * far) / (far + near - z * (far - near));
}
vec4 sobel(vec2 offsetex) {
    float kernel[9] = float[](1, 2, 1, 0, 0, 0, -1, -2, -1);

    vec2 mosaicInSize =(vec2(1.0,1.0)/InSize)*4.0;
    vec2 offset[9] = vec2[](
    vec2(-mosaicInSize.x, mosaicInSize.y),
    vec2(0, mosaicInSize.y),
    vec2(mosaicInSize.x, mosaicInSize.y),
    vec2(-mosaicInSize.x, 0),
    vec2(0, 0),
    vec2(mosaicInSize.x, 0),
    vec2(-mosaicInSize.x, -mosaicInSize.y),
    vec2(0, -mosaicInSize.y),
    vec2(mosaicInSize.x, -mosaicInSize.y)
    );

        float Gx = 0.0;
        float Gy = 0.0;
    for (int i = 0; i < 9; i++) {
        float intensity;
        vec2 coord = offsetex + offset[i];
        coord.x = clamp(coord.x, 0, 1);
        coord.y = clamp(coord.y, 0, 1);
        vec3 sampleVar = vec3(1,1,1);
        if(texture(FriendSampler, coord).b>(texture(FriendSampler, coord).r+texture(FriendSampler, coord).g)*40.0&&
        (texture(FriendDepthSampler,coord).r>=texture(TrueDepthSampler,coord).r||texture(FriendDepthSampler,coord).r==1.0)) {
            sampleVar=vec3(0,0,0);
        }
        intensity = (sampleVar.r + sampleVar.g + sampleVar.b);

        if (i != 4) {
            Gx += intensity * kernel[i];
        }
        int j = (i % 3) * 3 + i / 3;
        if (j != 4) {
            Gy += intensity * kernel[j];
        }
    }
    if(abs(Gy)>0.0||abs(Gx)>0.0){
        return vec4(1,1,1,1);
    }
    float G =0.0;
    float edgeThreshold = 0.0;
    float alpha = G > edgeThreshold ? 1.0 : 0.0;
    return vec4(G, G, G, alpha);
}
void main() {
    float depth = LinearizeDepth(texture(TrueDepthSampler, texCoord).r);

    float distance = length(vec3(1., (2.*texCoord - 1.) * vec2(InSize.x/InSize.y,1.) * tan(radians(_FOV / 2.))) * depth);

    vec2 mosaicInSize = InSize / 4;
    vec2 offsettex = texCoord;
    offsettex-=fract(offsettex*mosaicInSize)/mosaicInSize-0.5/mosaicInSize;
    fragColor = texture(DiffuseSampler,texCoord);
    if(texture(DiffuseDepthSampler, texCoord).r>0.98&&texture(FriendDepthSampler,offsettex).r<texture(TrueDepthSampler,offsettex).r){
        float sobeled = sobel(offsettex).a;
        fragColor=texture(FriendSampler,texCoord);
        float alphad = 1.0f-fragColor.a;
        fragColor.b-=alphad;

        if(sobeled>0.0){
            fragColor=vec4(1,1,1,1);
        }
        else if(fragColor.a>0.0){
            fragColor.rgb+=texture(DiffuseSampler,texCoord).rgb*alphad;
            fragColor.a=1.0f;
        }
    }
    if(fragColor.a<=0.0){
        fragColor = texture(DiffuseSampler,texCoord);
        fragColor.a = 1.0f;
    }
    fragColor.a = 1.0f;

}
