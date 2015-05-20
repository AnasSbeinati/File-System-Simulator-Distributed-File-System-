import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;


public interface tech {
	public boolean creatFile(Directory dir,String name,int sizeKB,HashMap<String,ArrayList<Boolean> > state);
	public boolean createDir(Directory dir,String name);
	public int deleteFile(Directory dir,String name,HashMap<String,ArrayList<Boolean> > state);
	public int deleteDir(Directory dir,HashMap<String, ArrayList<Boolean>> state);
	public void write(system sys, String filePath);
}
