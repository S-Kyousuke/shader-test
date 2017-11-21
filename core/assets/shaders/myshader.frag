#ifdef GL_ES
#define LOW lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOW
#define HIGH
#endif

uniform sampler2D u_texture;

uniform vec2 resolution;
uniform float sceneRatio;
uniform float softness;
uniform float radius;
uniform float darkness;
uniform vec2 position;

varying vec2 v_texCoord;
varying vec4 v_color;

float getScale(float ratio);

void main() {
    vec2 originPosition = (gl_FragCoord.xy / resolution.xy) - position;
    float ratio = resolution.x / resolution.y;
    float adjustedRadius = radius * getScale(ratio);
    float adjustedSoftness = softness * getScale(ratio);
    originPosition.x *= ratio;
    float len = length(originPosition);
    float vignette = smoothstep(adjustedRadius, adjustedRadius - adjustedSoftness, len);

    vec4 texColor = texture2D(u_texture, v_texCoord);
    texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, darkness);
    gl_FragColor = texColor * v_color;
}

float getScale(float ratio) {
    float ratioMultiplier = ratio / sceneRatio;
    return mix(ratioMultiplier, 1.0, step(1.0, ratioMultiplier));
}

