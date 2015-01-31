package board;

import java.util.*;

//
// represents mapping between id and matrix (raw_id)
//  (multiple matries corresponds to single id)
//
public class Pattern {
	int id;
	int[][] matrix;
	Pattern(int id, int[][] matrix){
		this.matrix = matrix;
		this.id = id;
	}
	

	
	static ArrayList<Pattern> patterns = null;
	static Pattern[] raw_id_to_pattern = null;
	static int[][] raw_id_to_pattern_id_and_orientation = null;
	
	static Pattern get_pattern(int id){
		if (patterns == null)
			init();
		return patterns.get(id);
	}
	static int[] get_id_and_orientation(int[][] matrix){
		if (patterns == null)
			init();
		
		int m = 1; 
	
		int raw_id = 0;
		for(int x=0; x<3; x++)
			for(int y=0; y<3; y++){
				raw_id += matrix[x][y] * m;
				m*=2;
			}
		return raw_id_to_pattern_id_and_orientation[raw_id];
	}
	/*
	static int get_id(int[][] matrix){
		if (patterns == null)
			init();
		
		int m = 1; 
	
		int raw_id = 0;
		for(int x=0; x<3; x++)
			for(int y=0; y<3; y++){
				raw_id += matrix[x][y] * m;
				m*=2;
			}
		return raw_id_to_pattern[raw_id].id;
	}
	*/
	
	static void init(){
		patterns = new ArrayList<Pattern>();
		raw_id_to_pattern = new Pattern[512];
		raw_id_to_pattern_id_and_orientation = new int[512][2];
		
		for(int raw_id=0; raw_id<512; raw_id++){
			int[] bits = new int[9];
			int m = 1;
			for(int level=0; level<9; level++){
				bits[level] = (raw_id / m)  - (raw_id/m/2)*2;
				m*=2;
			}

			int[][] matrix = new int[3][3];
			for(int x=0; x<3; x++)
				for(int y=0; y<3; y++){
						matrix[x][y] = bits[x*3+y];
				}
			//print_matrix("copy", matrix);
			
			if (is_identical_to_rotated(matrix, matrix, null))
				continue;
			
			Pattern new_pattern = null;
			int[] orientation = {0};	// no rotation
			for(Pattern pattern : patterns){
				if (is_identical(matrix, pattern.matrix, orientation)){
					new_pattern = pattern;
					break;
				}
			}
			if (new_pattern == null){
				new_pattern = new Pattern( patterns.size(), matrix);
				patterns.add(new_pattern);
			}
			
			raw_id_to_pattern[raw_id] = new_pattern;
			raw_id_to_pattern_id_and_orientation[raw_id][0] = new_pattern.id;
			raw_id_to_pattern_id_and_orientation[raw_id][1] = orientation[0];
		
		}
		System.out.println("# patterns = "+patterns.size());
	}
	static boolean is_identical(int[][] master, int[][] copy, int[] orientation){
		if (is_identical_no_rotation(master, copy)){
			if (orientation != null)
				orientation[0] = 0;
			return true;
		}
		return is_identical_to_rotated(master, copy, orientation);
	}
	static boolean is_identical_to_rotated(int[][] master, int[][] copy, int[] orientation){
		for(int i=1; i<4; i++){
			int[][] rotated = rotate(copy, i);
			if (is_identical_no_rotation(master, rotated)){
				if (orientation != null)
					orientation[0] = i;
				return true;
			}
		}
		return false;
	}
	static void print_matrix(String string, int[][] m){
		System.out.println(""+string);
		for(int x=0; x<3; x++){
			for(int y=0; y<3; y++)
				System.out.print(" "+m[x][y]);
			System.out.println("");
		}
		
	}
	static boolean is_identical_no_rotation(int[][] master, int[][] copy){
		for(int x=0; x<3; x++)
			for(int y=0; y<3; y++)
				if (master[x][y] != copy[x][y])
					return false;
		return true;
	}
	
	// 1 = PI/2 
	static int[][] rotate(int[][] original, int angle){
		if (angle == 0)
			return original;
		
		int[][] rotated = new int[3][3];
		int cos=0; 
		int sin=0;
		if (angle == 0){
			cos = 1;
			sin = 0;
		}
		if (angle == 1){
			cos = 0;
			sin = 1;
		}
		if (angle == 2){
			cos = -1;
			sin = 0;
		}
		if (angle == 3){
			cos = 0;
			sin = -1;
		}
		for(int x=-1; x<=1; x++)
			for(int y=-1; y<=1; y++){
				int u = cos*x + sin*y;
				int v = -sin*x + cos*y;
				rotated[x+1][y+1] = original[u+1][v+1];
			}
		return rotated;
		
	}
	
}
