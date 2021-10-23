package project2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Project2 {
	@SuppressWarnings("unused")
	public static void main (String[] args) {
		try {  
			// open the text file to be translated
			File inp = new File(args[0]); 
			String outFileName = ("Program1.txt".split("\\."))[0];			
			
			// create new output file in Java and set up code
			File out = new File(outFileName + ".java"); 
			FileWriter writer = new FileWriter(outFileName + ".java");
		    initialCode(writer, outFileName);
		    
		    // translate the input file to Java and write to 
		    //new output file
			parseFile(inp, writer);
			
			writer.write("\n\t}\n}");
			writer.close();
		} catch(Exception e) {  
			e.printStackTrace();  
		}  
	}
	
	public static void parseFile(File inp, FileWriter w) {
		try {
			w.write("\tSystem.out.println(\"Hello World\");\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void initialCode(FileWriter w, String name) {
		try {
			w.write("\npublic class " + name + "{\n");
			w.write("\n\tpublic static void main(String[] args){\n\t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// ML variable assignment (assuming integer)
//	Pattern p1 = Pattern.compile("val (.+) = (\\d+);");
//	String s1 = "val varName = 789;";
//	Matcher m1 = p1.matcher(s1);
//	if(m1.find()) {
//		System.out.println(m1.group(0));
//		System.out.println(m1.group(1));
//		System.out.println(m1.group(2));
//	}
//	// ML variable assignment for integer or real
//	Pattern p2 = Pattern.compile("val (.+) = ((\\d+)|(\\d+\\.\\d+));");
//	String s2 = "val x = 5.5;";
//	Matcher m2 = p2.matcher(s2);
//	if(m2.find()) {
//		System.out.println(m2.group(0));
//		System.out.println(m2.group(1));
//		System.out.println(m2.group(2));
//	}
}
