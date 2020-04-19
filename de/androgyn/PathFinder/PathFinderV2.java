package de.androgyn.PathFinder;

import java.util.ArrayList;
import java.util.List;

import de.androgyn.util.BlockPointer;
import net.minecraft.world.WorldProvider;

//new PathFinder(...).FinalWayStack;
public class PathFinderV2 {
	public ArrayList<BlockPointer> FinalWayStack;
	public static List<BlockPointer> currentProgessMirror = null;
	public static List<BlockPointer> currentProgessAllEntrysMirror = null;
	public static boolean timelapse = false;
	int target_x, target_y, target_z;
    int errorCountdown;
	public PathFinderV2(int start_x, int start_y, int start_z, int dest_x, int dest_y, int dest_z, int renderDist) {
		// OpenList = new ArrayList<BlockPointer>();
		// NextOpenList = new ArrayList<BlockPointer>();
		// AvailableList = new ArrayList<BlockPointer>();
		
		List<BlockPointer> AvailableList = BlockPointer.mapBlocks(renderDist);// loadPossibleBlocks(wp, true);
		// for(int i=AvailableList.size()-1; i>=0; i--) if(AvailableList.get(i).pos_y !=
		// start_y) AvailableList.remove(i);
		System.out.println("BlockPointer.mapBlocks OK");
		ArrayList<BlockPointer> temp = new ArrayList<BlockPointer>();
		temp.addAll(AvailableList);
		currentProgessAllEntrysMirror = temp;
		temp = new ArrayList<BlockPointer>();
		temp.addAll(AvailableList);
		// for(BlockPointer b : AvailableList) if(b.pos_y != 3) temp.remove(b);
		ArrayList<BlockPointer> basic_route = run(temp, start_x, start_y, start_z, dest_x, dest_y, dest_z);
		int restartPointer = -1, last_restartPointer = -1;
		ArrayList<BlockPointer> finalizedBlockLocations = new ArrayList<BlockPointer>();
		int maxLoopSystem = basic_route.size() / 4;
		
		int limitedLoopCount = 0;//3;
		if (maxLoopSystem > limitedLoopCount)
			maxLoopSystem = limitedLoopCount;

		
		

		while (maxLoopSystem-- > 0) {
			ArrayList<String> last_route_locations = new ArrayList<String>();
			for (BlockPointer b : basic_route) {
				last_route_locations.add(b.getPositionString());
				b.recoursive_way_cost = 0;
			}
			// temp = new ArrayList<BlockPointer> ();
			temp.clear();
			temp.addAll(AvailableList);
			int a = tryToOptimize(restartPointer, basic_route, temp, start_x, start_y, start_z, dest_x, dest_y, dest_z);
			// if (a == -1) {
			int z = 0;
			for (BlockPointer b : basic_route) {
				if (last_route_locations.contains(b.getPositionString()) && z < a) {
					finalizedBlockLocations.add(b);
				} else {
					break;
				}
				z++;
			}
		}




		for (BlockPointer b : basic_route)
			finalizedBlockLocations.add(b);
		ArrayList<BlockPointer> stack2 = new ArrayList<BlockPointer>();

		for (int i = finalizedBlockLocations.size() - 1; i >= 0; i--) {
			stack2.add(finalizedBlockLocations.get(i));
		}

		// stack2.addAll(finalizedBlockLocations);
		FinalWayStack = stack2;

		// FinalWayStack = finalizedBlockLocations;

		basic_route = null;
		stack2 = null;
		// OpenList = null;
		// NextOpenList = null;
		AvailableList = null;
		System.gc();
	}

	int tryToOptimize(int continue_at, ArrayList<BlockPointer> stack, ArrayList<BlockPointer> AvailableList,
			int start_x, int start_y, int start_z, int dest_x, int dest_y, int dest_z) {
		// printDebugFrame(stack);
		if (continue_at == -1)
			continue_at = 1;
		if (continue_at > stack.size() - 5)
			continue_at = 1;
		System.gc();
		// delay(1000);
		// double maxLen = stack.get(0).recoursive_way_cost;
		// remove start & end
		// stack.remove(stack.size() - 1);
		// stack.remove(0);
		// for(BlockPointer unterbrecher : stack){
		double[] costTable = new double[stack.size()];
		for (int i = 0; i < stack.size() - 1; i++)
			costTable[i] = stack.get(i).recoursive_way_cost;
		for (int i = continue_at; i < stack.size() - 2; i += 4) {// i++
			// for (int i=stack.size() - 2; i > 0; i--) {
			BlockPointer unterbrecher = stack.get(i);
			// double preCost = unterbrecher.recoursive_way_cost;
			double preCost = costTable[i];
			double postCost = costTable[i] - preCost;
			ArrayList<BlockPointer> temp = new ArrayList<BlockPointer>();
			temp.addAll(AvailableList);
			ArrayList<BlockPointer> stack_left = run(temp, start_x, start_y, start_z, unterbrecher.pos_x,
					unterbrecher.pos_y, unterbrecher.pos_z);
			///////// printDebugFrame(stack_left);
			if (stack_left.get(0).recoursive_way_cost < preCost) {
//      println("stack_left.size()" + stack_left.size());
//      println("old cost:"+preCost+" new cost:" + stack_left.get(0).recoursive_way_cost);
				// finish stack & exit
				temp = new ArrayList<BlockPointer>();
				temp.addAll(AvailableList);
				unterbrecher = stack.get(i + 1);
				ArrayList<BlockPointer> stack_right = run(temp, unterbrecher.pos_x, unterbrecher.pos_y,
						unterbrecher.pos_z, dest_x, dest_y, dest_z);
				// printDebugFrame(stack_right);
				// stack_left.remove(0);
				// stack_left.remove(stack_left.size()-1);
				stack.clear();// rekursive override
				stack.addAll(stack_right);
				stack.addAll(stack_left);
//      printDebugFrame(stack, stack_left.get(0));
				return i - 1;
			} else { // nope, nothing to optimize on the beginning...and that about the other side?
				temp = new ArrayList<BlockPointer>();
				temp.addAll(AvailableList);
				// for (BlockPointer a : stack_left) println(a);
				unterbrecher = stack.get(i + 1);
				ArrayList<BlockPointer> stack_right = run(temp, unterbrecher.pos_x, unterbrecher.pos_y,
						unterbrecher.pos_z, dest_x, dest_y, dest_z);
				if (stack_right.get(0).recoursive_way_cost < postCost) {
					// ok, secound part can be shorter, so lets apply it and re-begin
					// stack_left.remove(stack_left.size()-1);
					stack.clear();// rekursive override
					stack.addAll(stack_right);
					stack.addAll(stack_left);
					return i - 1;
				}
			} // */
		}
		return -1;
	}

	BlockPointer get(ArrayList<BlockPointer> list, int x/* , int y */, int z) {
		for (BlockPointer a : list)
			if (a.pos_x == x && /* a.pos_y == y && */a.pos_z == z)
				return a;
		return null;
	}

	ArrayList<BlockPointer> run(ArrayList<BlockPointer> AvailableList, int start_x, int start_y, int start_z,
			int dest_x, int dest_y, int dest_z) {
		errorCountdown = 10000;
		// println("run(ArrayList<BlockPointer> AvailableList :
		// #"+AvailableList.size()+", int start_x="+start_x+", int start_y="+start_y+",
		// int start_z="+start_z+", int dest_x="+dest_x+", int dest_y="+dest_y+", int
		// dest_z="+dest_z+");");
		// println("run(#"+AvailableList.size()+", "+start_x+", "+start_y+",
		// "+start_z+", "+dest_x+", "+dest_y+", "+dest_z+");");
		/*
		 * try { Thread.sleep(1); }catch(Exception e) { e.printStackTrace(); }
		 */
		for (BlockPointer bp : AvailableList) {
			bp.getAirDistanceTo(dest_x, dest_y, dest_z);
			bp.last = null;
		}
		ArrayList<BlockPointer> openList = new ArrayList<BlockPointer>();
		ArrayList<BlockPointer> closedList = new ArrayList<BlockPointer>();
		for (BlockPointer a : AvailableList)
			if (a.local_way_cost == 5000)
				closedList.add(a);
		for (BlockPointer a : closedList)
			AvailableList.remove(AvailableList.indexOf(a));
		BlockPointer next = get(AvailableList, start_x, start_z);
		if (next == null) {
			System.out.println("null-entry in 'next' found.");
			return new ArrayList<BlockPointer>();
		}
		
		next.pos_y = start_y;
		
		openList.add(next);
		int output_counter = 0;
		while (openList.size() > 0) {
			errorCountdown--;
			if(errorCountdown <= 0) break;
			//for (int i = 0; i < openList.size(); i++) if (openList.get(i) == null) System.out.println("null-entry " + i + " found.");
			next = getNearest(openList);
			closedList.add(next);
			if (next.recoursive_way_cost >= 5000) {
				openList.remove(openList.indexOf(next));
				continue;
			}
			if (next.lastLengthCalc == 0) {
				break;
				/*
				 * ArrayList<BlockPointer> stack_img = BlockPointerHerachieToStack(next);
				 * printDebugFrame(stack_img); //next = get(AvailableList, start_x, start_y,
				 * start_z); next = next.last; continue;
				 */
			}
			openList.remove(openList.indexOf(next));
			// println(next);
			extend(next, openList, AvailableList, closedList);

			if(((output_counter++) % 50) == 0) {
				//currentProgessAllEntrysMirror = new ArrayList<BlockPointer>();
				//currentProgessAllEntrysMirror.addAll(openList);
				currentProgessMirror = BlockPointerHerachieToStack(next);//TODO debug cmd
			}
			//if(timelapse) delay(1000);
			//delay(50);
		}

		// TODO virtual-stack -> real stack
		// return null;

		ArrayList<BlockPointer> stack = BlockPointerHerachieToStack(next);
		// println(stack.size());
		return stack;
	}

	public static void delay(int l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	ArrayList<BlockPointer> BlockPointerHerachieToStack(BlockPointer next) {
		ArrayList<BlockPointer> stack = new ArrayList<BlockPointer>();
		while (next != null) {
			stack.add(next);
			// println("list -> " + next);
			next = next.last;
			// delay(1);
		}
		return stack;
	}

	double getHeightCost(BlockPointer source, BlockPointer destination) {
		int h1 = source.pos_y;
		int h2 = destination.pos_y;
		int diff = h2 - h1;
		h1 = diff < 0 ? -diff : diff;
		if (h1 < 2)
			return h1;
		if (diff >= 2)
			return 5000;
		if (diff >= -3)
			return 0;
		// diff = -diff;
		// return diff * diff;
		return 5000;
	}

	void extend(BlockPointer b, ArrayList<BlockPointer> openList, ArrayList<BlockPointer> AvailableList,
			ArrayList<BlockPointer> closedList) {// loads nearest blocks into OpenList
		// BlockPointer main = get(openList, b.pos_x, b.pos_y, b.pos_z);
		// ArrayList<BlockPointer> nextList = new ArrayList<BlockPointer>();
		BlockPointer a;
		for (int z = -1; z <= 1; z++) {
			for (int x = -1; x <= 1; x++) {
				if (x == 0 && z == 0)
					continue;
				if ((a = get(openList, b.pos_x + x, b.pos_z + z)) != null) {
					if (closedList.contains(a))
						continue;
					//a.calcFixed(b);
					//b = source
					//a = destination
					b.calcFixed(a);
					//double old_cost = a.recoursive_way_cost;
					double new_cost = a.applyCost(b);// + heightCost;
					
					if(x != 0 && z != 0) {
						//System.out.println("Crossover : " + b + " -> x: " + (b.pos_x + x) + ", z: " + (b.pos_z + z2));
						if(!BlockPointer.canPass(b.pos_x + x, b.pos_y, b.pos_z + 0, false) || !BlockPointer.canPass(b.pos_x + 0, b.pos_y, b.pos_z + z, false)) {
							System.out.println("Crossover blocked.");
							
							new_cost += 5000;
							//continue;
						}
					}
					
					if (new_cost >= a.recoursive_way_cost) continue;
					// println("old: " + old_cost + " new: " + new_cost);
					// if (new_cost < old_cost) continue;
					if (new_cost < 5000) a.setParent(b);
					//a.resetHeight();
					continue;
				} else if ((a = get(AvailableList, b.pos_x + x, b.pos_z + z)) != null) {
					AvailableList.remove(AvailableList.indexOf(a));
					if (closedList.contains(a)) continue;
					b.calcFixed(a);
					
					if(x != 0 && z != 0) {
						int z2 = z;
						System.out.println("Crossover : " + b + " -> x: " + (b.pos_x + x) + ", z: " + (b.pos_z + z2));
						if(!BlockPointer.canPass(b.pos_x + x, b.pos_y, b.pos_z + 0, false) || !BlockPointer.canPass(b.pos_x + 0, b.pos_y, b.pos_z + z, false)) {
							System.out.println("Crossover blocked.");
							a.recoursive_way_cost += 5000;
							//new_cost += 5000;
							//continue;
						}
					}
					
					
					//b.calcFixed(a);
					//a.calcFixed(b);
					// double new_cost = a.applyCost(b);
					// double old_cost = a.recoursive_way_cost;
					// a.recoursive_way_cost = new_cost;
					// if (new_cost >= old_cost) continue;

					// if(a.recoursive_way_cost >= 5000) continue;
					openList.add(a);

					if (a.recoursive_way_cost < 5000) a.setParent(b);

				}
			}
		}
		// for(BlockPointer bp : nextList){
		// bp.applyCost(b);
		// }
	}

	BlockPointer getNearest(ArrayList<BlockPointer> openList) {
		BlockPointer best = openList.get(0);
		double score = best.lastLengthCalc + best.recoursive_way_cost;
		for (BlockPointer b : openList) {
			double testscore = b.lastLengthCalc + b.recoursive_way_cost;
			if (testscore <= score) {
				score = testscore;
				best = b;
			}
		}
		return best;
	}
}