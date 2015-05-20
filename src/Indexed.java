import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Indexed implements tech {

	public Indexed() {
		super();
	}

	@Override
	public boolean creatFile(Directory dir, String name, int sizeKB,
			HashMap<String, ArrayList<Boolean>> state) {
		HashMap<String, ArrayList<Integer>> allocatedBlocks = new HashMap<String, ArrayList<Integer>>();
		for (Map.Entry<String, ArrayList<Boolean>> disk : state.entrySet()) {
			int i = 0;
			ArrayList<Integer> temp = new ArrayList<>();
			boolean tag = false;
			for (Boolean b : disk.getValue()) {
				if (!b) {
					if (!tag) {
						temp.add(i);
						disk.getValue().set(i, true);
						i++;
						tag = true;
						continue;
					}
					disk.getValue().set(i, true);
					temp.add(i);
					if (--sizeKB == 0)
						break;
				}
				i++;
			}
			if (!temp.isEmpty())
				allocatedBlocks.put(disk.getKey(), temp);
			if (sizeKB == 0) {
				File1 file = new File1(name, allocatedBlocks);
				dir.files.add(file);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean createDir(Directory dir, String name) {
		dir.subDirectory.add(new Directory(name));
		return true;
	}

	@Override
	public int deleteFile(Directory dir, String name,
			HashMap<String, ArrayList<Boolean>> state) {
		int totalSize = 0;
		for (File1 file : dir.files) {
			if (file.name.equals(name)) {
				for (Map.Entry<String, ArrayList<Integer>> disk : file.allocatedBlocks
						.entrySet())
					for (int i = 0; i < disk.getValue().size(); i++) {
						state.get(disk.getKey()).set(disk.getValue().get(i),
								false);
						totalSize++;
					}
				file.deleted = true;
				return totalSize;
			}
		}
		return 0;
	}

	@Override
	public int deleteDir(Directory dir,
			HashMap<String, ArrayList<Boolean>> state) {
		int totalspace = 0;
		for (File1 file : dir.files) {
			totalspace += deleteFile(dir, file.name, state);
		}
		for (Directory dire1 : dir.subDirectory) {
			totalspace += deleteDir(dire1, state);
		}
		dir.deleted = true;
		return totalspace;
	}

	public void write(system sys, String filePath) {
		try {
			FileOutputStream os = new FileOutputStream(new File(filePath));
			ObjectOutputStream ob = new ObjectOutputStream(os);
			ob.writeInt(sys.sizeKB);
			ob.writeInt(sys.allspace);
			ob.writeInt(sys.state.size());
			// write hashmap
			for (Map.Entry<String, ArrayList<Boolean>> disk : sys.state
					.entrySet()) {
				ob.writeObject(disk.getKey());
				ob.writeInt(disk.getValue().size());
				for (boolean bool : disk.getValue()) {
					ob.writeBoolean(bool);
				}
			}
			String currentPath = "root";
			writeTree(sys.root, ob, currentPath);
			ob.close();
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("out");
		}
	}

	private void writeTree(Directory dir, ObjectOutputStream ob,
			String currentPath) throws IOException {
		ob.writeObject(currentPath);
		ob.writeInt(dir.files.size());
		for (File1 file : dir.files) {
			ob.writeObject(currentPath + "\\" + file.name);
			ob.writeInt(file.allocatedBlocks.size());
			for (Map.Entry<String, ArrayList<Integer>> disk : file.allocatedBlocks
					.entrySet()) {
				{
					ob.writeObject(disk.getKey());
					ob.writeInt(disk.getValue().size());
					for (Integer in : disk.getValue()) {
						ob.writeInt(in);
					}
				}
			}
		}
		for (Directory dire : dir.subDirectory) {
			writeTree(dire, ob, currentPath + "\\" + dire.name);
		}
	}

	public static system read(String filePath) {
		FileInputStream in;
		try {
			in = new FileInputStream(new File(filePath));
			ObjectInputStream ob = new ObjectInputStream(in);
			system sys = new system();
			sys.setSizeKB(ob.readInt());
			sys.setAllspace(ob.readInt());
			HashMap<String, ArrayList<Boolean>> disks = new HashMap<>();
			int nDisks = ob.readInt();
			HashMap<String, Integer> disks1 = new HashMap<>();
			for (int i = 0; i < nDisks; i++) {
				ArrayList<Boolean> state = new ArrayList<>();
				String name = (String) ob.readObject();
				int size = ob.readInt();
				disks1.put(name, size);
				for (int j = 0; j < size; j++) {
					state.add(ob.readBoolean());
				}
				disks.put(name, state);
			}
			sys.setState(disks);
			sys.setDisks(disks1);
			sys.setTech1(new Indexed());
			readTree(sys, ob, 0, sys.getSizeKB());
			return sys;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void readTree(system sys, ObjectInputStream os,
			int currentSize, int sizeKB) throws ClassNotFoundException,
			IOException {
		if (currentSize < sizeKB - 1) {
			Object obj;
			try {
				obj = os.readObject();
			} catch (Exception e) {
				return;
			}
			String currentPath = (String) obj;
			String paths[];
			if (currentPath.equals("root")) {
				paths = new String[1];
				paths[0] = "root";
			} else
				paths = currentPath.trim().split("\\\\");
			if (!currentPath.equals("root"))
				sys.createFolder(currentPath);
			Directory dir = sys.getDire(sys.root, paths, 0);
			System.out.println(paths.length+" "+dir.name);
			//dir =sys.getDire1(dir, paths[paths.length-1]);
			int fileListSize = os.readInt();
			ArrayList<File1> files = new ArrayList<>();
			for (int i = 0; i < fileListSize; i++) {
				String s[] = ((String) os.readObject()).split("\\\\");
				HashMap<String, ArrayList<Integer>> allocatedBlocks = new HashMap<>();
				ArrayList<Integer> blocks = new ArrayList<>();
				int sized = os.readInt();
				int sizekb = 0;
				for (int j = 0; j < sized; j++) {
					String name = (String) os.readObject();
					int sizeb = os.readInt();
					sizekb += sizeb;
					for (int k = 0; k < sizeb; k++) {
						blocks.add(os.readInt());
					}
					allocatedBlocks.put(name, blocks);
				}
				File1 file = new File1(s[s.length - 1], allocatedBlocks);
				files.add(file);
				currentSize += sizekb;
			}
			dir.setFiles(files);
			readTree(sys, os, currentSize, sizeKB);
		}
	}
}
