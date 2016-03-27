//attribute float size;
attribute vec3 customColor;
attribute float cellState;

varying float factor;
varying vec3 color;

void main() {

    color = customColor;
    factor = cellState == 0.0 ? 0.5 : 1.0;

    vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );

    gl_PointSize = 14.0 * ( 300.0 / length( mvPosition.xyz ) );

    gl_Position = projectionMatrix * mvPosition;

}