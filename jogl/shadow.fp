#version 130
in vec4 flocation;
void main(){
	float depth = flocation.z;
	float y = fract(depth*100);
	float x = (depth*100-y)/100;
	gl_FragData[1]=vec4(x,y,flocation.z,1);
}
