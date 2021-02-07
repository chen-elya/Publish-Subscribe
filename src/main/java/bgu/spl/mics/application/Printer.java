package bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Printer {
    private static Gson gson=new Gson();

    public static void print(Object obj,String filename) {
        String s = filename +".json";
        try (Writer writer = new FileWriter(s)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(obj, writer);
        }
        catch (IOException ioe){
            System.out.println("Couldn't print to " + filename);
        }
    }
}
