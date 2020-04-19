package de.androgyn.util._3d;

import java.util.ArrayList;

import org.json.simple.JSONArray;

public class Object3D {
	public static final float DEG_TO_RAD = 0.017453292F;
	public static final float RAD_TO_DEG = 57.295776F;
	ArrayList<Face3D> faces;
	Vector3D relativeRotation;

	public Object3D() {
		faces = new ArrayList<Face3D>();
		relativeRotation = new Vector3D();
	}
	
	public Object3D add(Face3D f) {
		faces.add(f);
		return this;
	}
	

	public Object3D parse(JSONArray arr) {
		int len = arr.size();
		for (int i = 0; i < len; i++)
			faces.add(new Face3D().parse((JSONArray) arr.get(i)));
		return this;
	}

	public JSONArray save() {
		int i = 0;
		JSONArray out = new JSONArray();
		for (Face3D f : faces)
			out.set(i++, f.save());
		return out;
	}
	/*
	 * void draw(PGraphics pg) { for (Face3D f : faces) { pg.beginShape();
	 * f.draw(pg); pg.endShape(); } } PShape draw() { PShape out =
	 * createShape(GROUP); for (Face3D f : faces) out.addChild(f.draw());
	 * onDraw(out); return out; } void onDraw(PShape out) { }
	 */
	public void draw() {
		for (Face3D f : faces) f.draw();
	}
	
	public Object3D translate(Vector3D v) {
		return translate(v.x, v.y, v.z);
	}

	public Object3D translate(double x, double y, double z) {
		for (Face3D f : faces) {
			for (Vector3D v : f.voxels) {
				v.x += x;
				v.y += y;
				v.z += z;
			}
		}
		return this;
	}

	public Object3D rotateRelative(double x, double y, double z, boolean calcFormObjectCenterElseFromMatrixCenter) {
		double xx = relativeRotation.x - x;
		double yy = relativeRotation.y - y;
		double zz = relativeRotation.z - z;
		relativeRotation.add(new Vector3D(-xx, -yy, -zz));
		return this.rotate(xx, yy, zz, calcFormObjectCenterElseFromMatrixCenter);
	}

	public Object3D rotate(Vector3D v, boolean calcFormObjectCenterElseFromMatrixCenter) {
		return rotate(v.x, v.y, v.z, calcFormObjectCenterElseFromMatrixCenter);
	}

	public Object3D rotate(double x, double y, double z, boolean calcFormObjectCenterElseFromMatrixCenter) {
		Vector3D center = new Vector3D();
		if (calcFormObjectCenterElseFromMatrixCenter) {
			center = getCenter();
			translate(-center.x, -center.y, -center.z);
		}
		double angleX = x * DEG_TO_RAD;
		double angleY = y * DEG_TO_RAD;
		double angleZ = z * DEG_TO_RAD;

		for (Face3D f : faces) {
			for (Vector3D v : f.voxels) {

				double neuX = v.x;
				double neuY = v.y;
				double neuZ = v.z;
				double tmp = 0;
				tmp = (neuX * (-Math.sin(angleY))) + (neuZ * Math.cos(angleY));
				neuX = (Math.cos(angleY) * neuX) + (Math.sin(angleY) * neuZ);
				neuZ = tmp;
				tmp = (neuX * (-Math.sin(angleZ))) + (neuY * Math.cos(angleZ));
				neuX = (Math.cos(angleZ) * neuX) + (Math.sin(angleZ) * neuX);
				neuY = tmp;
				tmp = (neuY * (-Math.sin(angleX))) + (neuZ * Math.cos(angleX));
				neuY = (Math.cos(angleX) * neuY) + (Math.sin(angleX) * neuZ);
				neuZ = tmp;

				v.x = neuX;
				v.y = neuY;
				v.z = neuZ;
			}
		}

		if (calcFormObjectCenterElseFromMatrixCenter)
			translate(center.x, center.y, center.z);
		return this;
	}

	public Vector3D getCenter() {
		Vector3D faceCenter = new Vector3D();
		Vector3D objectCenter = new Vector3D();
		float count = 0;
		for (Face3D f : faces) {
			if (f.voxels.size() == 0)
				continue;
			else
				count++;
			objectCenter = new Vector3D();
			for (Vector3D v : f.voxels) {
				objectCenter.add(v);
			}
			faceCenter.add(objectCenter.mult(1f / (float) f.voxels.size()));
		}
		return count == 0 ? new Vector3D() : objectCenter.mult(1f / count);
	}

	public Vector3D[] getRawVectors() {
		ArrayList<Vector3D> voxels = new ArrayList<Vector3D>();
		for (Face3D f : faces)
			for (Vector3D v : f.voxels)
				voxels.add(v);
		return (Vector3D[]) voxels.toArray();
	}
}
