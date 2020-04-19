package de.androgyn.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import de.androgyn.util.glBuilder.GLFactory3D;
import de.androgyn.util.glBuilder.GLFace3D;
import de.androgyn.util.glBuilder.GLVector3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DrawEngine {

	private static final Minecraft mc = Minecraft.getMinecraft();
	private static int counter=0;
	public static void startDrawing(){
		glPushMatrix();
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_LINE_SMOOTH);
		glDepthMask(false);
		glDisable(GL_DEPTH_TEST);
	}
	
	public static void stopDrawing(){
		glEnd();
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_LINE_SMOOTH);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
	
	public static void translatePlayer() {
		glTranslated(-mc.getRenderManager().viewerPosX,
				-mc.getRenderManager().viewerPosY,
				-mc.getRenderManager().viewerPosZ);
	}
	
	public static void setDepthTest(boolean value) {
		if (value) glDisable(GL_DEPTH_TEST);
		else glEnable(GL_DEPTH_TEST);
	}
	
	
	
	public static void drawArea(boolean depthtest, int r, int g, int b, double alpha, Vec3d vec) {		
		GLFactory3D.getNewFactory()
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5, 0, -0.5))
					.add(new GLVector3D(-0.5, 0, 0.5))
					.add(new GLVector3D(0.5, 0, 0.5))
					.add(new GLVector3D(0.5, 0, -0.5))
					.setMode(GL_QUADS)
					.setColor(new Color(r,g,b))
					.setTransparency(alpha))
			.setDepthTest(depthtest)
			.translate(vec.x, vec.y, vec.z)
			.translate(0.5, 0, 0.5)
			.translatePlayer()
			.draw();
	}
	
	
	public static void drawTest(boolean depthtest, int r, int g, int b, Vec3d vec) {
		GLFactory3D.getNewFactory()
				.add(new GLFace3D()
						.add(new GLVector3D(0.5, -0.5, -0.5))
						.add(new GLVector3D(0.5, -0.5, 0.5))
						.add(new GLVector3D(-0.5, -0.5, 0.5))
						.add(new GLVector3D(-0.5, -0.5, -0.5))
						.setMode(GL_QUADS)
						.setColor(new Color(r, g, b))
						)
				.add(new GLFace3D()
						.add(new GLVector3D(0, 0.5, -0.5))
						.add(new GLVector3D(0, 0.5, 0.5))
						.add(new GLVector3D(0.5, -0.5, 0.5))
						.add(new GLVector3D(0.5, -0.5, -0.5))
						.setMode(GL_QUADS)
						.setColor(new Color(r, g, b))
						)
				.add(new GLFace3D()
						.add(new GLVector3D(-0.5, -0.5, -0.5))
						.add(new GLVector3D(-0.5, -0.5, 0.5))
						.add(new GLVector3D(0, 0.5, 0.5))
						.add(new GLVector3D(0, 0.5, -0.5))
						.setMode(GL_QUADS)
						.setColor(new Color(r, g, b))
						)
				.add(new GLFace3D()
						.add(new GLVector3D(-0.5, -0.5, -0.5))
						.add(new GLVector3D(0, 0.5, -0.5))
						.add(new GLVector3D(0.5, -0.5, -0.5))
						.setMode(GL_TRIANGLES)
						.setColor(new Color(r,g,b))
						)
				.add(new GLFace3D()
						.add(new GLVector3D(0.5, -0.5, 0.5))
						.add(new GLVector3D(0, 0.5, 0.5))
						.add(new GLVector3D(-0.5, -0.5, 0.5))
						.setMode(GL_TRIANGLES)
						.setColor(new Color(r,g,b))
						)
				.setDepthTest(false)
				.rotate(180, counter += 1, 0, true)
				.translate(vec.x, vec.y, vec.z)
				.setTransparencyToAllFaces(0.3)
				.translate(0.5, 3+0.5, 0.5)
				.translatePlayer()
				.draw();
	}
	
	public static void drawBlockOutline(int r, int g, int b, double alpha, Vec3d vec) {
		drawBlockOutline(r, g, b, alpha, vec, false);
	}
	public static void drawBlockOutline(int r, int g, int b, double alpha, Vec3d vec, boolean depth_test) {
		GLFactory3D.getNewFactory()
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,-0.5,-0.5))
					.add(new GLVector3D(-0.5,+0.5,-0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(+0.5,-0.5,-0.5))
					.add(new GLVector3D(+0.5,+0.5,-0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(+0.5,-0.5,+0.5))
					.add(new GLVector3D(+0.5,+0.5,+0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,-0.5,+0.5))
					.add(new GLVector3D(-0.5,+0.5,+0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,+0.5,-0.5))
					.add(new GLVector3D(+0.5,+0.5,-0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,+0.5,+0.5))
					.add(new GLVector3D(+0.5,+0.5,+0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,+0.5,-0.5))
					.add(new GLVector3D(-0.5,+0.5,+0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(+0.5,+0.5,-0.5))
					.add(new GLVector3D(+0.5,+0.5,+0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,-0.5,-0.5))
					.add(new GLVector3D(+0.5,-0.5,-0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,-0.5,+0.5))
					.add(new GLVector3D(+0.5,-0.5,+0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5,-0.5,-0.5))
					.add(new GLVector3D(-0.5,-0.5,+0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(+0.5,-0.5,-0.5))
					.add(new GLVector3D(+0.5,-0.5,+0.5)))
			.setModeToAllFaces(GL_LINES)
			.setColorToAllFaces(new Color(r,g,b))
			.setTransparencyToAllFaces(alpha)
			.setDepthTest(depth_test)
			.translate(vec.x, vec.y, vec.z)
			.translate(0.5, 0.5, 0.5)
			.translatePlayer()
			.draw();
	}
	
	public static void drawArrow3D(double size, int r, int g, int b, Vec3d vec, float xSize, float ySize, float zSize, int rotation) {
		double thick = (double)size/100;
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		double ox = player.posX-0.5;
		double oy = player.posY-1.1;
		double oz = player.posZ-0.5;
		
		double top_xF = 1;
		double top_yF = 1;
		double top_zF = 1;
		double tip_xF = 1;
		double tip_yF = 1;
		double tip_zF = 1;
		
		double tipwidth=1.2;
		double tipTopxWidth=0.5;
		double tipTopzWidth=0.5;
		
		if (rotation > 0) tipTopxWidth=tipwidth;
		else tipTopzWidth=tipwidth;
		
		double topOffset = 3.0;
		
		glPushMatrix();
		glTranslated(0, 0, 0);
		glColor3f(r/255f, g/255f, b/255f);
		glBegin(GL_QUADS);
		
		//TOP
		
		//top_top face		
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz-thick-zSize);
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz-thick-zSize);

		//top_left face
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy-thick-ySize+topOffset, vec.z-oz+thick+zSize+top_zF);
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy-thick-ySize+topOffset, vec.z-oz-thick-zSize-top_zF);
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz-thick-zSize-top_zF);
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz+thick+zSize+top_zF);

		//top_right face
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy-thick-ySize+topOffset, vec.z-oz-thick-zSize-top_zF);
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy-thick-ySize+topOffset, vec.z-oz+thick+zSize+top_zF);
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz+thick+zSize+top_zF);
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy+thick+ySize+topOffset, vec.z-oz-thick-zSize-top_zF);
		
		//top_front face		
		glVertex3d(vec.x-ox+thick+xSize+top_zF, vec.y-oy-thick-ySize+topOffset, vec.z-oz-thick-zSize);
		glVertex3d(vec.x-ox-thick-xSize-top_zF, vec.y-oy-thick-ySize+topOffset, vec.z-oz-thick-zSize);
		glVertex3d(vec.x-ox-thick-xSize-top_zF, vec.y-oy+thick+ySize+topOffset, vec.z-oz-thick-zSize);
		glVertex3d(vec.x-ox+thick+xSize+top_zF, vec.y-oy+thick+ySize+topOffset, vec.z-oz-thick-zSize);
				
		//top_back face
		glVertex3d(vec.x-ox-thick-xSize-top_zF, vec.y-oy-thick-ySize+topOffset, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox+thick+xSize+top_zF, vec.y-oy-thick-ySize+topOffset, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox+thick+xSize+top_zF, vec.y-oy+thick+ySize+topOffset, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox-thick-xSize-top_zF, vec.y-oy+thick+ySize+topOffset, vec.z-oz+thick+zSize);
		
		
		
		//TIP
		
		//Top face		
		glVertex3d(vec.x-ox-thick-xSize-tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz+thick+zSize+tipTopzWidth);
		glVertex3d(vec.x-ox-thick-xSize-tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz-thick-zSize-tipTopzWidth);
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz-thick-zSize-tipTopzWidth);
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz+thick+zSize+tipTopzWidth);
		
		//left face
		glVertex3d(vec.x-ox, vec.y-oy-thick-ySize, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox, vec.y-oy-thick-ySize, vec.z-oz-thick-zSize);
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy+thick+ySize, vec.z-oz-thick-zSize);
		glVertex3d(vec.x-ox+thick+xSize, vec.y-oy+thick+ySize, vec.z-oz+thick+zSize);
                   
		//right face
		glVertex3d(vec.x-ox, vec.y-oy-thick-ySize, vec.z-oz-thick-zSize);
		glVertex3d(vec.x-ox, vec.y-oy-thick-ySize, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy+thick+ySize, vec.z-oz+thick+zSize);
		glVertex3d(vec.x-ox-thick-xSize, vec.y-oy+thick+ySize, vec.z-oz-thick-zSize);
		           
		//back face
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz+thick+zSize+tipTopzWidth);
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz+thick+zSize+tipTopzWidth);
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy+thick+ySize, vec.z-oz+thick+zSize+tipTopzWidth);
		glVertex3d(vec.x-ox-thick-xSize-tipTopxWidth, vec.y-oy+thick+ySize, vec.z-oz+thick+zSize+tipTopzWidth);
		           
		//front face		
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz-thick-zSize-tipTopzWidth);
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy-thick-ySize, vec.z-oz-thick-zSize-tipTopzWidth);
		glVertex3d(vec.x-ox-thick-xSize-tipTopxWidth, vec.y-oy+thick+ySize, vec.z-oz-thick-zSize-tipTopzWidth);
		glVertex3d(vec.x-ox+thick+xSize+tipTopxWidth, vec.y-oy+thick+ySize, vec.z-oz-thick-zSize-tipTopzWidth);
		
		
		glPopMatrix();
	}

	public static void drawRect3D(int r, int g, int b, double a, double x, double y, double z, Vec3d center, boolean depthTest) {
		
		GLFactory3D.getNewFactory()
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5, +0.5, -0.5))
					.add(new GLVector3D(-0.5, +0.5, +0.5))
					.add(new GLVector3D(+0.5, +0.5, +0.5))
					.add(new GLVector3D(+0.5, +0.5, -0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5, -0.5, +0.5))
					.add(new GLVector3D(-0.5, -0.5, -0.5))
					.add(new GLVector3D(+0.5, -0.5, -0.5))
					.add(new GLVector3D(+0.5, -0.5, +0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(+0.5, -0.5, -0.5))
					.add(new GLVector3D(-0.5, -0.5, -0.5))
					.add(new GLVector3D(-0.5, +0.5, -0.5))
					.add(new GLVector3D(+0.5, +0.5, -0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(+0.5, -0.5, +0.5))
					.add(new GLVector3D(+0.5, -0.5, -0.5))
					.add(new GLVector3D(+0.5, +0.5, -0.5))
					.add(new GLVector3D(+0.5, +0.5, +0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5, -0.5, -0.5))
					.add(new GLVector3D(-0.5, -0.5, +0.5))
					.add(new GLVector3D(-0.5, +0.5, +0.5))
					.add(new GLVector3D(-0.5, +0.5, -0.5)))
			.add(new GLFace3D()
					.add(new GLVector3D(-0.5, -0.5, +0.5))
					.add(new GLVector3D(+0.5, -0.5, +0.5))
					.add(new GLVector3D(+0.5, +0.5, +0.5))
					.add(new GLVector3D(-0.5, +0.5, +0.5)))
			.setColorToAllFaces(new Color(r,g,b))
			.setTransparencyToAllFaces(a)
			.setModeToAllFaces(GL_QUADS)
			.translate(center.x, center.y, center.z)
			.translate(0.5, 0.5, 0.5)
			.translatePlayer()
			.setDepthTest(depthTest)
			.draw();
	}
	
	public static void drawLine(boolean depth_test, int red, int green, int blue, Vec3d... blockPoses) {
		if(blockPoses.length > 1) {
			startDrawing();
			translatePlayer();
			glTranslated(0,0,0);
			setDepthTest(depth_test);
			glColor3ub((byte)red, (byte)green, (byte)blue);
			Vec3d lastBlockPose = null;
			boolean a = false;
			glTranslated(0.5, 1.1, 0.5);
			glBegin(GL_LINES);
			for(int i=1; i<blockPoses.length; i++) {
				glVertex3d(blockPoses[i-1].x, blockPoses[i-1].y, blockPoses[i-1].z);
				glVertex3d(blockPoses[i].x, blockPoses[i].y, blockPoses[i].z);
			}
			stopDrawing();
		}
	}
	
	private static void bindSkinTexture(String name) {
		ResourceLocation location = AbstractClientPlayer.getLocationSkin(name);
		try {
			ThreadDownloadImageData img = AbstractClientPlayer.getDownloadImageSkin(location, name);
			img.loadTexture(Minecraft.getMinecraft().getResourceManager());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static void rect(int x, int y, int w, int h, int r, int g, int b, int saturation) {
		  int _x = x, _y = y;
		  int missing_x = w, missing_y = h;
		  while(missing_y > 0){
		    while(missing_x > 0){
		      Minecraft.getMinecraft().draw(_x, _y, 11*16, 13*16, limitVal(missing_x, 16), limitVal(missing_y, 16), r, g, b, saturation);
		      missing_x -= 16;
		      _x +=16;
		    }
		    missing_x = w;
		    _x = x;
		    missing_y -= 16;
		    _y += 16;
		  }
		}

		public static int limitVal(int a, int b){
		  return a > b ? b : a;
		}
}
