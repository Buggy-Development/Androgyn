package de.androgyn.util;

import java.util.ArrayList;
import java.util.List;

public class PathOptimizer {

  public static void shortenPath(List<BlockPointer> path){
    List<BlockPointer> output = new ArrayList<BlockPointer>();
    BlockPointer currentLocation = path.remove(0);
    int[] lastvec = new int[]{0, 0, 0};
    for(BlockPointer  b : path) {
      int[] vec = BlockPointerToVector(currentLocation, b);
      if(!testIntArray(vec, lastvec)){
        output.add(b);
        lastvec = vec;
      }
      currentLocation = b;
     }
    path.clear();
    path.addAll(output);
  }

  public static int[] BlockPointerToVector(BlockPointer a, BlockPointer b){
    return new int[]{
      a.pos_x - b.pos_x,
      a.pos_y - b.pos_y,
      a.pos_z - b.pos_z
    };
  }

  public static boolean testIntArray(int[] a, int[] b){
    if(a.length != b.length) return false;
    for(int i=0; i<a.length; i++) if(a[i] != b[i]) return false;
    return true;
  }

}