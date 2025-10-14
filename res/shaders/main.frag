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

void main(void) {
	
	if(goraudEnabled == 1) {
		if(calculatedFogDensity >= 0.99) { // discard if way past in the fog
			discard;
		} else {
			vec4 fragColor = mix((pass_textureCoords.xy, (texture(textureSampler, pass_textureCoords) * calculatedLightColour)), vec4(0.4140625,0.390625,0.4375,1.0), calculatedFogDensity);
			
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