package de.androgyn.util.glBuilder;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

public class GLFactory3D {

	private static final float DEG_TO_RAD = 0.017453292F;
	private static final float RAD_TO_DEG = 57.295776F;
	private List<GLFace3D> faces = new ArrayList<GLFace3D>();
	private boolean depthtest = false;
	
	public static GLFactory3D getNewFactory() {
		return new GLFactory3D();
	}
	
	public GLFactory3D add(GLFace3D face) {
		faces.add(face);
		return this;
	}
	
	public void draw() {

		glPushMatrix();
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_LINE_SMOOTH);
		glDepthMask(false);
		if (!depthtest) glEnable(GL_DEPTH_TEST);
		else glDisable(GL_DEPTH_TEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		for (GLFace3D face : faces) {
			face.draw();
		}
		
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_LINE_SMOOTH);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
	
	public GLFactory3D setDepthTest(boolean depthtest) {
		this.depthtest = depthtest;
		return this;
	}
	
	public GLFactory3D setModeToAllFaces(int mode) {
		for (GLFace3D face : faces) {
			face.setMode(mode);
		}
		return this;
	}
	
	public GLFactory3D setColorToAllFaces(Color color) {
		for (GLFace3D face : faces) {
			face.setColor(color);
		}
		return this;
	}
	
	public GLFactory3D setTransparencyToAllFaces(double alpha) {
		for (GLFace3D face : faces) {
			face.setTransparency(alpha);
		}
		return this;
	}
	
	public GLFactory3D translatePlayer() {
		Minecraft mc = Minecraft.getMinecraft();
		translate(-mc.getRenderManager().viewerPosX,
				-mc.getRenderManager().viewerPosY,
				-mc.getRenderManager().viewerPosZ);
		return this;
	}
	
	public GLFactory3D translate(double x, double y, double z) {
		for (GLFace3D face : faces) {
			for (GLVector3D vertex : face.getVertexs()) {
				vertex.x +=x;
				vertex.y +=y;
				vertex.z +=z;
			}
		}
		return this;
	}
	
	public GLFactory3D rotate(double x, double y, double z, boolean centerFromObject) {
		GLVector3D center = new GLVector3D();
		if (centerFromObject) {
			center = getCenter();
			translate(-center.x, -center.y, -center.z);
		}
		double angleX = x * DEG_TO_RAD;
		double angleY = y * DEG_TO_RAD;
		double angleZ = z * DEG_TO_RAD;
		
		for (GLFace3D face : faces) {
			for (GLVector3D vertex : face.getVertexs()) {
				double x2 = vertex.x;
				double y2 = vertex.y;
				double z2 = vertex.z;
				double tmp = 0;
				
				tmp = (x2*(-Math.sin(angleY))) + (z2*Math.cos(angleY));
				x2 = (Math.cos(angleY) * x2) + (Math.sin(angleY) * z2);
				z2 = tmp;
				tmp = (x2 * (-Math.sin(angleZ))) + (y2 * Math.cos(angleZ));
				x2 = (Math.cos(angleZ) * x2) + (Math.sin(angleZ) * x2);
				y2 = tmp;
				tmp = (y2 * (-Math.sin(angleX))) + (z2 * Math.cos(angleX));
				y2 = (Math.cos(angleX) * y2) + (Math.sin(angleX) * z2);
				z2 = tmp;
				
				/*
				 * tmp = (neuX * (-Math.sin(angleY))) + (neuZ * Math.cos(angleY));
				neuX = (Math.cos(angleY) * neuX) + (Math.sin(angleY) * neuZ);
				neuZ = tmp;
				tmp = (neuX * (-Math.sin(angleZ))) + (neuY * Math.cos(angleZ));
				neuX = (Math.cos(angleZ) * neuX) + (Math.sin(angleZ) * neuX);
				neuY = tmp;
				tmp = (neuY * (-Math.sin(angleX))) + (neuZ * Math.cos(angleX));
				neuY = (Math.cos(angleX) * neuY) + (Math.sin(angleX) * neuZ);
				neuZ = tmp;
				 */
				vertex.x = x2;
				vertex.y = y2;
				vertex.z = z2;
			}
		}

		if (centerFromObject) {
			translate(center.x, center.y, center.z);
		}
		
		return this;
	}
	
	private GLVector3D getCenter() {
		GLVector3D faceCenter = new GLVector3D();
		GLVector3D objectCenter = new GLVector3D();
		float count = 0;
		for (GLFace3D face : faces) {
			if (face.getVertexs().size() == 0)
				continue;
			else
				count++;
			objectCenter = new GLVector3D();
			for (GLVector3D v : face.getVertexs()) {
				objectCenter.add(v);
			}
			faceCenter.add(objectCenter.mult(1f / (float) face.getVertexs().size()));
		}
		return count == 0 ? new GLVector3D() : objectCenter.mult(1f / count);
	}
	

}
