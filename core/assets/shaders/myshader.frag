uniform sampler2D u_texture;

varying vec2 v_texCoord;

void main() {
    gl_FragColor = vec4(1, 0, 0, 1) * texture2D(u_texture, v_texCoord);
}