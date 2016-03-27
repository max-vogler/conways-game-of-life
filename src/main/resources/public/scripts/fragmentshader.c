varying vec3 color;
uniform sampler2D texture;

varying float factor;

void main() {

    gl_FragColor = vec4( color * factor, 1.0 );
    gl_FragColor = gl_FragColor * texture2D( texture, gl_PointCoord );

    if ( gl_FragColor.a < ALPHATEST ) discard;

}