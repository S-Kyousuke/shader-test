uniform mat4 u_projTrans;

attribute vec4 a_position;

void main() {
    gl_Position = u_projTrans * a_position;
}