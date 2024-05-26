varying lowp vec2 TextureCoordsOut;
uniform sampler2D Texture;
void main(void) {
    gl_FragColor = texture2D(Texture, TextureCoordsOut);
}


#define SlotVertexData 0
#define SlotVertexUniforms 1

#define SlotFragmentUniforms 2