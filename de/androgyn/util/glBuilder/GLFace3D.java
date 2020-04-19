package de.androgyn.util.glBuilder;

import static org.lwjgl.opengl.GL11.glEnd;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class GLFace3D {

	private List<GLVector3D> vertexs = new ArrayList<GLVector3D>();
	private int mode = GL11.GL_QUADS; //Default mode
	private Color color = new Color(0, 0, 0);
	private double transparency = 1;
	
	public GLFace3D() {
		
	}
	
	public GLFace3D add(GLVector3D vertex) {
		vertexs.add(vertex);
		return this;
	}
	
	public GLFace3D setMode(int mode) {
		this.mode = mode;
		return this;
	}
	
	public GLFace3D setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public GLFace3D setTransparency(double transparency) {
		this.transparency = transparency;
		return this;
	}
	
	public List<GLVector3D> getVertexs() {
		return vertexs;
	}
	
	public void draw() {
		GL11.glColor4d((double)color.getRed()/255, (double)color.getGreen()/255, (double)color.getBlue()/255, transparency);
		GL11.glBegin(mode);
		for (GLVector3D v : vertexs) {
			GL11.glVertex3d(v.x, v.y, v.z);
		}
		glEnd();
	}
	
}
