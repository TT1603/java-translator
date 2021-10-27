package project2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Project2 {
	@SuppressWarnings("unused")
	public static void main (String[] args) {
		try {  
			// open the text file to be translated
			String inFileName = args[0];
			File inp = new File(inFileName); 
			FileReader fr = new FileReader(inp);
			BufferedReader br = new BufferedReader(fr);
			
			
			// create new output file in Java and set up code
			String outFileName = (inFileName.split("\\."))[0];			
			File out = new File(outFileName + ".java"); 
			FileWriter writer = new FileWriter(outFileName + ".java");
		    initialCode(writer, outFileName);
		    
		    
		    // translate the input file to Java and write to 
		    //new output file
			parseFile(writer, br);
			
			
			// finalize code and close file
			writer.write("}\n}");
			writer.close();
			br.close();
			fr.close();
			
		} catch(Exception e) {  
			e.printStackTrace();  
		}  
	}
	
	public static void parseFile(FileWriter w, BufferedReader br) {
		try {
			String line;  
			while((line = br.readLine()) != null) {
				
				// take command-line arguments: 
				// var <variable> = args[<integer>];
				// NOTES: variable names are alphabetic
				Pattern p1 = Pattern.compile("var (([a-z]+)|([A-Z]+)) = args\\[(\\d+)\\];");
				Matcher m1 = p1.matcher(line);
				if(m1.find()) {						
					System.out.println(m1.group(0));
					w.write("int "+ m1.group(1)+" = arg["+m1.group(2)+"];\n");
					continue;
				}
					
				
				// print string_literals to output
				// <print> ::= print <string_literal>;
				Pattern p2 = Pattern.compile("print \"(.*)\";");
				Matcher m2 = p2.matcher(line);
					
				if(m2.find()) {
					System.out.println(m2.group(0));		
					w.write("System.out.println(\"" + m2.group(1) + "\");\n");
					continue;
				}
				
				
				// print variables to output
				// <print> ::= print <variable>;
				Pattern p3 = Pattern.compile("print (([a-z]+)|([A-Z]+));");
				Matcher m3 = p3.matcher(line);
					
				if(m3.find()) {
					System.out.println(m3.group(0));		
					w.write("System.out.println(" + m3.group(1) + ");\n");
					continue;
				}
				
				
				// comments
				// <comment> ::= //<string> 
				Pattern p4 = Pattern.compile("//(.*)");
				Matcher m4 = p4.matcher(line);
					
				if(m4.find()) {
					System.out.println(m4.group(0));		
					w.write("//" + m4.group(1) + "\n");
					continue;
				}
				
				
				// variable assignment for integer, real, variable, array element:
				// var <variable> = <integer> | <real> | <variable> | <arr_access>;
				Pattern p5 = Pattern.compile("var ([a-z]+|[A-Z]+) = (\\d+|\\d+\\.\\d+|[a-z]+|[A-Z]+|([a-z]+|[A-Z])\\[(\\d+)\\]);");
				Matcher m5 = p5.matcher(line);
					
				if(m5.find()) {
					System.out.println(m5.group(0));		
					w.write("int "+ m5.group(1) + " = " + m5.group(2)+";\n");
					continue;
				}
				
				// integer expressions
				
				// boolean expressions
				
				// array operations
				
				// conditionals
				
				// loops
								
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void initialCode(FileWriter w, String name) {
		try {
			w.write("\npublic class " + name + "{\n");
			w.write("\npublic static void main(String[] args){\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
