attribute vec2 Positions;
attribute vec2 TextureCoords;
varying vec2 TextureCoordsOut;

void main(void) {
    gl_Position = vec4(Positions[0], Positions[1], 0.0, 1.0);
    TextureCoordsOut = TextureCoords;
}

