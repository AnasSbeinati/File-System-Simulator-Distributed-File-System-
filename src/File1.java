import java.util.ArrayList;
import java.util.HashMap;


public class File1 {
	String name;
	boolean deleted;
	HashMap<String,ArrayList<Integer>> allocatedBlocks;
	public File1() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String,ArrayList<Integer>> getAllocatedBlocks() {
		return allocatedBlocks;
	}
	public void setAllocatedBlocks(HashMap<String,ArrayList<Integer>> allocatedBlocks) {
		this.allocatedBlocks = allocatedBlocks;
	}
	public File1(String name, HashMap<String,ArrayList<Integer>> allocatedBlocks) {
		this.name = name;
		this.allocatedBlocks = allocatedBlocks;
		deleted=false;
	}
}
