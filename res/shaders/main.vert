#version 150

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out float pass_affine;

out vec3 surfaceNormal;
out vec3 toLightVector;

out vec4 calculatedLightColour;
out float calculatedFogDensity;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform vec3 lightColour;
uniform int goraudEnabled;
uniform vec2 targetResolution;

void main(void) {
	
	mat4 matrixCalculations = projectionMatrix * viewMatrix * transformationMatrix;
	
	vec4 vertInClipSpace = matrixCalculations * vec4(position, 1.0);
	
	vec4 snapToPixel = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
	vec4 vertex = snapToPixel;
	vertex.xyz = snapToPixel.xyz / snapToPixel.w;
	
	vec2 grid = vec2(100,75);
	
	vertex.x = floor(grid.x * vertex.x) / grid.x;
	vertex.y = floor(grid.y * vertex.y) / grid.y;
	vertex.xyz *= snapToPixel.w;
	
	gl_Position = vertex;
	pass_textureCoords = textureCoords;
	
	surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
	
	if (goraudEnabled == 1) {
		// heavily modified goraud vertex shader
		// original source: https://github.com/JChan2787/Goraud_Phong_Shading/blob/master/gouraud.vert.glsl
		
		vec4 ambient = vec4(0.2, 0.2, 0.2, 1.0);
		ambient = vec4(0.4140625,0.390625,0.4375,1);
		vec4 diffuse = vec4(0.5, 0.5, 0.5, 1.0);
		diffuse = vec4(0.2,0.2,0.2,1);
	
		toLightVector = (transformationMatrix * vec4(position,1.0)).xyz - lightPosition;
		vec4 matrixedLightPosition = matrixCalculations * vec4(lightPosition,1.0);
		
		//Getting the direction vector for both light-sources
		vec3 dirLight = toLightVector;
		
		
		//Normalizing the direction vectors
		//Getting the dot product of N & L
		float nDot = dot(normalize(surfaceNormal), normalize(-dirLight));
		
		vec4 positionRelativeToCamera = viewMatrix * (transformationMatrix * vec4(position, 1.0));
		float depth = length(positionRelativeToCamera.xyz);
		
		calculatedFogDensity = clamp((15 - depth) / (0 - 15), 0.0, 1.0);
		
		vec4 diffuseLightCalculation = clamp(diffuse * vec4((lightColour*3),1.0) * max(nDot, 0.0), 0.0, 1.0); //Diffuse
		
		calculatedLightColour = (ambient*2) + diffuseLightCalculation; // Sum the colors and pass it along to the fragment shader.
	} else {
		toLightVector = lightPosition - (transformationMatrix * vec4(position,1.0)).xyz;
	}
	
}