uniform sampler2D u_texture;

varying vec2 v_texCoord;
varying vec4 v_color;

void main() {
    gl_FragColor = vec4(vec3(v_texCoord.s), 1.0);
    //gl_FragColor = vec4(vec3(v_texCoord.t), 1.0);
}