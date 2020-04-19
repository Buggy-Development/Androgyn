package de.androgyn.util._3d;

import org.json.simple.JSONObject;

public class Vector3D {
  public double x, y, z;
  public Vector3D() {
    x = 0D;
    y = 0D;
    z = 0D;
  }
  public Vector3D(double xx, double yy, double zz) {
    x = xx;
    y = yy;
    z = zz;
  }
  public Vector3D mult(double a) {
    x *= a;
    y *= a;
    z *= a;
    return this;
  }
  public Vector3D add(Vector3D a) {
    x += a.x;
    y += a.y;
    z += a.z;
    return this;
  }
  public Vector3D parse(JSONObject obj) {
    x = (double) obj.get("x");
    y = (double) obj.get("y");
    z = (double) obj.get("z");
    return this;
  }
  public JSONObject save() {
    JSONObject out = new JSONObject();
    out.put("x", x);
    out.put("y", y);
    out.put("z", z);
    return out;
  }
  public Vector3D rotate(Vector3D v) {
    return rotate(v.x, v.y, v.z);
  }
  public Vector3D rotate(double x, double y, double z) {
  
    double angleX = x;// * DEG_TO_RAD;
    double angleY = y;// * DEG_TO_RAD;
    double angleZ = z;// * DEG_TO_RAD;

    Vector3D v = this;
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
    return this;
  }
}