#version 150

in vec2 pass_textureCoords;
in float pass_affine;

in vec3 surfaceNormal;
in vec3 toLightVector;


in vec4 calculatedLightColour;
in float calculatedFogDensity;

out vec4 out_Colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform int goraudEnabled;

float dither8x8(vec2 position, float brightness) {
  int x = int(mod(position.x, 4.0));
  int y = int(mod(position.y, 4.0));
  int index = x + y * 4;
  float limit = 0.0;

  if (x < 8) {
    if (index == 0) limit = 0.0625;
    if (index == 1) limit = 0.5625;
    if (index == 2) limit = 0.1875;
    if (index == 3) limit = 0.6875;
    if (index == 4) limit = 0.8125;
    if (index == 5) limit = 0.3125;
    if (index == 6) limit = 0.9375;
    if (index == 7) limit = 0.4375;
    if (index == 8) limit = 0.25;
    if (index == 9) limit = 0.75;
    if (index == 10) limit = 0.125;
    if (index == 11) limit = 0.625;
    if (index == 12) limit = 1.0;
    if (index == 13) limit = 0.5;
    if (index == 14) limit = 0.875;
    if (index == 15) limit = 0.375;
  }

  return brightness < limit ? 0.0 : 1.0;
}

vec4 dither(vec2 position, vec4 color) {
  return vec4(color.rgb * dither8x8(position, dot(color.rgb, vec3(0.299, 0.587, 0.114))), 1.0);
}

void main(void) {
	
	if(goraudEnabled == 1) {
		if(calculatedFogDensity >= 0.99) { // discard if way past in the fog
			discard;
		} else {
			vec4 fragColor = mix(dither(pass_textureCoords.xy, (texture(textureSampler, pass_textureCoords) * calculatedLightColour)), vec4(0.4140625,0.390625,0.4375,1.0), calculatedFogDensity);
			
			float levels = 10.0;
			float greyscale = max(fragColor.r, max(fragColor.g, fragColor.b));
			
			float lower = floor(greyscale * levels) / levels;
			float lowerDiff = abs(greyscale - lower);
			
			float upper = ceil(greyscale * levels) / levels;
			float upperDiff = abs(upper - greyscale);
			
			float level= lowerDiff <= upperDiff ? lower : upper;
			float adjustment = level / greyscale;
			
			fragColor.rgb = fragColor.rgb * adjustment;
			
			//fragColor.rgb = pow(fragColor.rgb, vec3(gamma.x));
			
			out_Colour = fragColor;
			
			//out_Colour = texture(textureSampler, pass_textureCoords) * calculatedLightColour;
		}
	} else {
		vec3 unitNormal = normalize(surfaceNormal);
		vec3 unitLightVector = normalize(toLightVector);
		
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
		vec3 diffuse = brightness * lightColour;
		
		out_Colour = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords);
	}
		
}