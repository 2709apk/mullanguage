#version 130  
in vec4 fcolor; 
in vec4 lightnor;
in vec4 viewloc; 
in vec4 lightloc;
uniform sampler2D tex1;
uniform sampler2D tex0;
void main() 
{
	// float dp0 = texture(tex1,vec2(lightloc.s,lightloc.t)).z;
	vec2 xy = texture(tex1,vec2(lightloc.s,lightloc.t)).xy;
	float dp0 = xy.y/100+xy.x;

	float dt = -(lightloc.z-dp0);	//黑白比例中 白的比例
	dt = min(max(100*(dt+0.01)+1,0),1);
	//漫反射
	vec4 diffuse = (dt*max(0,-normalize(lightnor.xyz).z*(1/0.95)-0.05)+(1-dt)*vec4(0,0,0,0)+0.2)*texture(tex0,fcolor.st);
	//高光
	// vec4 highlight = ;
	
	gl_FragData[0] = diffuse;
	gl_FragData[2]=vec4(0,1,0,1);
}
// // float dp0 = texture(tex1,vec2(lightloc.s,lightloc.t)).z;
// 	vec2 xy = texture(tex1,vec2(lightloc.s,lightloc.t)).xy;
// 	float dp0 = xy.y/100+xy.x;

// 	float dt = -(lightloc.z-dp0);	//黑白比例中 白的比例
// 	dt = min(max(100*(dt+0.01)+1,0),1);
// 	//漫反射
// 	vec4 diffuse = (dt*max(0,dot(normalize(lightnor.xyz),vec3(0,0,-1)))+(1-dt)*0+0.2)*texture(tex0,fcolor.st);
// 	//高光
// 	// vec4 highlight = ;
	
// 	gl_FragData[0] = diffuse;
// 	gl_FragData[2]=vec4(0,1,0,1);