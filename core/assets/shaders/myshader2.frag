uniform sampler2D u_texture;

varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    texColor.rgb = 1 - texColor.rgb;
    gl_FragColor = v_color * texColor;
}