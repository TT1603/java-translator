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
			
			//take agrs in : var x = args[0]
			Pattern p1 = Pattern.compile("var (.+) = args\\[(\\d+)\\];");
			String s1 = "var x = args[0];";
			Matcher m1 = p1.matcher(s1);
				if(m1.find()) {
					
				System.out.println(m1.group(0));
				System.out.println(m1.group(1));
				
				writer.write("int "+ m1.group(1)+" = arg["+m1.group(2)+"];\n");
			}
				
			//variable assignment for integer or real Pattern 
			Pattern p2 = Pattern.compile("var (.+) = ((\\d+)|(\\d+\\.\\d+));");
			String s2 = "var a = 5.5;";
			Matcher m2 = p2.matcher(s2);
				
			if(m2.find()) {
				System.out.println(m2.group(0));
				System.out.println(m2.group(1));
				System.out.println(m2.group(2));
					
				writer.write("int "+ m1.group(1) + " = " + m1.group(2)+";\n");
			}
			
			Pattern p3 = Pattern.compile("if (.+) (+|-|*|/|%) (.+) == (\\d+);");
			String s3 = "command in: args";
			Matcher m3 = p3.matcher(s3);
			
			if(m3.find()) {
				System.out.println(m3.group(0));
				System.out.println(m3.group(1));
				System.out.println("if "+ m3.group(1) + " = " + m3.group(2) + m3.group(3));
			}
			
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
	
	

		
/**		
		//command in: args;  
		Pattern p3 = Pattern.compile("command in: args;");
		String s3 = "command in: args;";
		Matcher m3 = p3.matcher(s3);
		
		if(m3.find()) {
			System.out.println("public static void main (String[] " + args + ") {");
		}
		

	
		
		//if...then: if i % x == 0 then count = count + 1


		
		}
	}
*/

}
