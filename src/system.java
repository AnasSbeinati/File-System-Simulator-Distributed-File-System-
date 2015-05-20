import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

public class system {
	int sizeKB, allspace = 0;
	tech tech1;
	HashMap<String, Integer> disks;
	Directory root;
	HashMap<String, ArrayList<Boolean>> state;
	
	public system(){
		root = new Directory("root");
	}
	public int getSizeKB() {
		return sizeKB;
	}

	public void setSizeKB(int sizeKB) {
		this.sizeKB = sizeKB;
	}

	public tech getTech1() {
		return tech1;
	}

	public void setTech1(tech tech1) {
		this.tech1 = tech1;
	}

	public HashMap<String, Integer> getDisks() {
		return disks;
	}

	public void setDisks(HashMap<String, Integer> disks) {
		this.disks = disks;
	}

	public int getAllspace() {
		return allspace;
	}

	public void setAllspace(int allspace) {
		this.allspace = allspace;
	}

	public Directory getRoot() {
		return root;
	}

	public void setRoot(Directory root) {
		this.root = root;
	}

	public HashMap<String, ArrayList<Boolean>> getState() {
		return state;
	}

	public void setState(HashMap<String, ArrayList<Boolean>> state) {
		this.state = state;
	}

	public system(int sizeKB, tech tech1, HashMap<String, Integer> disks) {
		this.sizeKB = sizeKB;
		this.tech1 = tech1;
		state = new HashMap<String, ArrayList<Boolean>>();
		for (Map.Entry<String, Integer> entry : disks.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			ArrayList<Boolean> temp = new ArrayList<Boolean>();
			for (int i = 0; i < value; ++i)
				temp.add(false);
			state.put(key, temp);
		}
		root = new Directory("root");
		this.disks = disks;
	}

	public boolean createFile(String path, int sizeKB) {
		if (sizeKB+1 >= this.sizeKB - this.allspace)
			return false;
		String[] paths = path.trim().split("\\\\");
		Directory iter;
		iter = getDire(root, paths, 0);
		if (iter != null) {
			if (tech1.creatFile(iter, paths[paths.length - 1], sizeKB, state)) {
				this.allspace += sizeKB+1;
				return true;
			} else
				return false;
		} else
			return false;
	}

	public boolean createFolder(String path) {
		String[] paths = path.trim().split("\\\\");
		Directory iter;
		iter = getDire(root, paths, 0);
		if (iter != null)
			return tech1.createDir(iter, paths[paths.length - 1]);
		else
			return false;
	}

	public boolean deleteFile(String path) {
		String[] paths = path.trim().split("\\\\");
		Directory iter;
		iter = getDire(root, paths, 0);
		if (iter != null) {
			int fileSize = tech1.deleteFile(iter, paths[paths.length - 1],
					state);
			if (fileSize != 0) {
				this.allspace -= fileSize;
				return true;
			}
			return false;
		} else
			return false;
	}

	public boolean deleteFolder(String path) {
		String[] paths = path.trim().split("\\\\");
		Directory iter;
		iter = getDire(root, paths, 0);
		iter=getDire1(iter,paths[paths.length-1]);
		if (iter != null) {
			int filesSize = tech1.deleteDir(iter, state);
			if (filesSize != 0) {
				this.allspace -= filesSize;
				return true;
			}
			return false;
		} else
			return false;
	}

	public Directory getDire(Directory dir, String[] path, int level) {
		if (path.length == 1)
			return dir;
		for (Directory temp : dir.subDirectory) {
			if (path[level + 1].equals(temp.name) && level != path.length - 2) {
				return getDire(temp, path, level + 1);
			}
			if (path[level+1].equals(temp.name) && level == path.length - 2) {
				return temp;
			}
		}
		if (path[level].equals(dir.name) && level == path.length - 2) {
			return dir;
		}
		return null;
	}

	public Directory getDire1(Directory dir, String name) {
		for (Directory temp : dir.subDirectory) {
			if(temp.name.equals(name))
				return temp;
		}
		return null;
	}

	public void DisplayDiskStructure() {
		root.printDirectoryStructure(0);
	}

	public void DisplayDiskStatus() {
		System.out.println("Empty space:\n" + (sizeKB - allspace) + " KB");
		System.out.println("Allocated space:\n" + allspace + " KB");
		System.out.println("Empty Blocks in the Disk:");
		for (Map.Entry<String, ArrayList<Boolean>> disk : state.entrySet()) {
			System.out.println(disk.getKey() + ": ");
			for (int i = 0; i < disk.getValue().size(); i++) {
				System.out.println("Block:[" + i + "] is "
						+ (disk.getValue().get(i) ? "Allocated" : "Empty"));
			}
		}
	}
}
