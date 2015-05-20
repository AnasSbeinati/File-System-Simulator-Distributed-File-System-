import java.util.HashMap;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		String lock = "1";
		String command = "";
		tech t;
		system sys;
		System.out.println("Type your commands");
		while (!command.equals("exit")) {
			command = new Scanner(System.in).nextLine();
			String str[] = command.trim().split(" ");
			if (str[0].equals("createsystem")) {
				System.out.println("Type Disks");
				HashMap<String, Integer> disks = new HashMap<>();
				int totalSize = 0;
				for (int i = 0; i < Integer.parseInt(str[1]); i++) {
					String name = new Scanner(System.in).nextLine();
					int size = new Scanner(System.in).nextInt();
					disks.put(name, size);
					totalSize += size;
				}
				t = new Indexed();
				sys = new system(totalSize, t, disks);
				System.out.println("->System has been initialized");
				Main(sys);
				t.write(sys, "C:\\Users\\Anoos\\Desktop\\.metadata.vsf");
			} else if (str[0].equals("load")) {
				try {
					sys = Indexed
							.read("C:\\Users\\Anoos\\Desktop\\.metadata.vsf");
					System.out
							.println("->System has been loaded enter your commands");
					Main(sys);
				} catch (Exception e) {
					System.out.println("->ther is no system to load!");
				}
			} else if (!str[0].equals("exit")) {
				System.out.println("->Wrong command try again");
			}
		}
	}

	public static void Main(system sys) {
		String command = "";
		while (!command.equals("exit")) {
			command = new Scanner(System.in).nextLine();
			String str[] = command.trim().split(" ");
			switch (str[0]) {
			case "createfile":
				if (sys.createFile(str[1], Integer.parseInt(str[2])))
					System.out.println("->file has been created");
				else
					System.out.println("->file was not created");
				break;
			case "createfolder":
				sys.createFolder(str[1]);
				System.out.println("->folder has been created");
				break;
			case "deletefolder":
				sys.deleteFolder(str[1]);
				System.out.println("->folder has been deleted");
				break;
			case "deletefile":
				sys.deleteFile(str[1]);
				System.out.println("->file has been deleted");
				break;
			case "status":
				sys.DisplayDiskStatus();
				break;
			case "structure":
				sys.DisplayDiskStructure();
				break;
			case "exit":
				break;
			default:
				System.out.println("->No match command");
			}
		}
	}
}
