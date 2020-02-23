package cn.bluedog;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SLAPI implements Serializable
{
    public static <T extends Object> void save(T obj,String path) throws Exception
    {
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
    oos.writeObject(obj);
    oos.flush();
    oos.close();
    }

    public static <T extends Object> T load(String path) throws Exception
    {
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
    T result = (T)ois.readObject();
    ois.close();
    return result;
    }
}