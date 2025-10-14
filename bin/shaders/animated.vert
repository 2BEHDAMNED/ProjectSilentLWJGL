#version 150

const int MAX_JOINTS = 50;//max joints allowed in a skeleton
const int MAX_WEIGHTS = 3;//max number of joints that can affect a vertex

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in ivec3 in_jointIndices;
in vec3 in_weights;

out vec2 pass_textureCoords;
out float pass_affine;

out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 jointTransforms[MAX_JOINTS];
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec4 calculatedLightColour;
out float calculatedFogDensity;

uniform vec3 lightPosition;
uniform vec3 lightColour;
uniform int goraudEnabled;
uniform vec2 targetResolution;

void main(void){
	
	vec4 totalLocalPos = vec4(0.0);
	vec4 totalNormal = vec4(0.0);
	
	for(int i=0;i<MAX_WEIGHTS;i++){
		mat4 jointTransform = jointTransforms[in_jointIndices[i]];
		vec4 posePosition = jointTransform * vec4(position, 1.0);
		totalLocalPos += posePosition * in_weights[i];
		
		vec4 worldNormal = jointTransform * vec4(normal, 0.0);
		totalNormal += worldNormal * in_weights[i];
	}
	
	mat4 matrixCalculations = projectionMatrix * viewMatrix * transformationMatrix;
	
	vec4 vertInClipSpace = matrixCalculations * totalLocalPos;
	
	vec4 snapToPixel = projectionMatrix * viewMatrix * transformationMatrix * totalLocalPos;
	vec4 vertex = snapToPixel;
	vertex.xyz = snapToPixel.xyz / snapToPixel.w;
	
	vec2 grid = vec2(60,45);
	
	vertex.x = floor(grid.x * vertex.x) / grid.x;
	vertex.y = floor(grid.y * vertex.y) / grid.y;
	vertex.xyz *= snapToPixel.w;
	
	gl_Position = vertex;
	pass_textureCoords = textureCoords;
	
	surfaceNormal = (transformationMatrix * totalNormal).xyz;
	
	if (goraudEnabled == 1) {
		// heavily modified goraud vertex shader
		// original source: https://github.com/JChan2787/Goraud_Phong_Shading/blob/master/gouraud.vert.glsl
		
		vec4 ambient = vec4(0.2, 0.2, 0.2, 1.0);
		ambient = vec4(0.4140625,0.390625,0.4375,1);
		vec4 diffuse = vec4(0.5, 0.5, 0.5, 1.0);
		diffuse = vec4(0.2,0.2,0.2,1);
	
		toLightVector = (transformationMatrix * totalLocalPos).xyz - lightPosition;
		vec4 matrixedLightPosition = matrixCalculations * vec4(lightPosition,1.0);
		
		//Getting the direction vector for both light-sources
		vec3 dirLight = toLightVector;
		
		//Normalizing the direction vectors
		//Getting the dot product of N & L
		float nDot = dot(normalize(surfaceNormal), normalize(-dirLight));
		
		vec4 positionRelativeToCamera = viewMatrix * (transformationMatrix * totalLocalPos);
		float depth = length(positionRelativeToCamera.xyz);
		
		calculatedFogDensity = clamp((15 - depth) / (0 - 15), 0.0, 1.0);
		
		vec4 diffuseLightCalculation = clamp(diffuse * vec4((lightColour*3),1.0) * max(nDot, 0.0), 0.0, 1.0); //Diffuse
		
		calculatedLightColour = (ambient*2) + diffuseLightCalculation; // Sum the colors and pass it along to the fragment shader.
	} else {
		toLightVector = lightPosition - (transformationMatrix * totalLocalPos).xyz;
	}
	
}