package de.androgyn.util._3d;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;

public class Face3D {
	public ArrayList<Vector3D> voxels;

	public Face3D() {
		voxels = new ArrayList<Vector3D>();
	}
	
	public Face3D add(Vector3D a) {
		voxels.add(a);
		return this;
	}

	public Face3D parse(JSONArray arr) {
		int len = arr.size();
		for (int i = 0; i < len; i++)
			voxels.add(new Vector3D().parse((JSONObject) arr.get(i)));
		return this;
	}

	public JSONArray save() {
		int i = 0;
		JSONArray out = new JSONArray();
		for (Vector3D v : voxels)
			out.set(i++, v.save());
		return out;
	}

	public void draw() {
		//if(voxels.size() == 3) GL11.glBegin(GL11.GL_TRIANGLES);
		//if(voxels.size() == 4) GL11.glBegin(GL11.GL_QUADS);
		//GL11.glPushMatrix();
		//GL11.glBegin(GL11.GL_TRIANGLES);
		for (Vector3D v : voxels) {
			GL11.glVertex3d(v.x, v.y, v.z);
		}
		//GL11.glEnd();
		//GL11.glPopMatrix();
	}
}