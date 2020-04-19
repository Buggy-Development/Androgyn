package de.androgyn.util.glBuilder;

public class GLVector3D {

	public double x, y, z;

	public GLVector3D() {
		x = 0;
		y = 0;
		z = 0;
	}

	public GLVector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public GLVector3D mult(double a) {
		x *= a;
		y *= a;
		z *= a;
		return this;
	}

	public GLVector3D add(GLVector3D a) {
		x += a.x;
		y += a.y;
		z += a.z;
		return this;
	}

}
