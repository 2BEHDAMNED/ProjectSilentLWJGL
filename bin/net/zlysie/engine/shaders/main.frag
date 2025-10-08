#version 150

in vec2 pass_textureCoords;

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
		out_Colour = mix((texture(textureSampler, pass_textureCoords) * calculatedLightColour), vec4(0.4140625,0.390625,0.4375,1.0), calculatedFogDensity);
		//out_Colour = texture(textureSampler, pass_textureCoords) * calculatedLightColour);
	} else {
		vec3 unitNormal = normalize(surfaceNormal);
		vec3 unitLightVector = normalize(toLightVector);
		
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
		vec3 diffuse = brightness * lightColour;
		
		out_Colour = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords);
	}
		
}