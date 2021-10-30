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
				
				/** basic components: integer, real, boolean
					variable names, string literals
				*/
				Pattern integer = Pattern.compile("(\\d+)");
				Pattern real = Pattern.compile("(\\d+)\\.(\\d+)");
				Pattern bool = Pattern.compile("True|False");
				Pattern variable = Pattern.compile("([a-zA-Z]+)");
				Pattern string = Pattern.compile("\\\"(.*)\\\"");
				
				
				//-----------------------------------------
				/** Expression types: arithmetic (int, real), boolean,
					array declaration/access
				
					1) Arithmetic
					<expr_int> ::= <expr_int> + <mul_expr> | <expr_int> - <mul_expr> | <mul_expr>
					<mul_expr> ::= <mul_expr> * <neg_expr> | <mul_expr> / <neg_expr> | <mul_expr> % <neg_expr> | <neg_expr>
					<neg_expr> ::= - <i_root> | <i_root> 
					<i_root> ::= <integer> | <real> | <variable> | sqrt(<expr_int>)
				*/
				Pattern iRoot = Pattern.compile(real+"|"+integer+"|"+variable);
				Pattern neg = Pattern.compile("-"+iRoot+"|"+iRoot);
				Pattern mul = Pattern.compile(neg +" * "+ neg+"|"+neg +" \\/ "+ neg+"|"+neg +" \\% "+ neg+"|"+neg);
				
				Matcher m = mul.matcher(line);
				if(m.find()) {						
					System.out.println(m.group(0));
					w.write(m.group(1) + ", " + m.group(2));
					continue;
				}
				// TO BE FILLED
				
				
				/** 2) boolean
					<expr_bool> ::= <expr_bool> or <and_expr> | <and_expr>
					<and_expr> ::= <and_expr> and <not_expr> | <not_expr>
					<not_expr> ::= not <b_root> | <b_root> 
					<b_root> ::= <boolean> | <expr_int> <operator> <expr_int> 
					<operator> ::= < | > | = | <= | >= | ==
				*/
				// TO BE FILLED
				
				
				/** 3) array operations
					<arr_expr> ::= [<arr_element>] | []
					<arr_element> ::= <arr_element>,<variable> | <arr_element>,<integer> | <arr_element>, <boolean> | <variable> | <integer> | <boolean>
					<arr_access> ::= <variable>[<integer>]
				*/
				// TO BE FILLED
				
				
				
				//-----------------------------------------
				// take command-line arguments: 

				// int <variable> = args[<integer>];
				Pattern pArgs1 = Pattern.compile("int " + variable + " = args\\[" + integer + "\\];");
				Matcher mArgs1 = pArgs1.matcher(line);
				if(mArgs1.find()) {						
					System.out.println(mArgs1.group(0));
					w.write("int " + mArgs1.group(1) + " = (int) arg[" + mArgs1.group(2) + "];\n");
					continue;
				}
				
				// real <variable> = args[<integer>];
				Pattern pArgs2 = Pattern.compile("real " + variable + " = args\\[" + integer + "\\];");
				Matcher mArgs2 = pArgs2.matcher(line);
				if(mArgs2.find()) {						
					System.out.println(mArgs2.group(0));
					w.write("float " + mArgs2.group(1) + " = (float) arg[" + mArgs2.group(2) + "];\n");
					continue;
				}
				
				// string <variable> = args[<integer>];
				Pattern pArgs3 = Pattern.compile("string " + variable + " = args\\[" + integer + "\\];");
				Matcher mArgs3 = pArgs3.matcher(line);
				if(mArgs3.find()) {						
					System.out.println(mArgs3.group(0));
					w.write("String " + mArgs3.group(1) + " = (int) arg[" + mArgs3.group(2) + "];\n");
					continue;
				}
				
				
				//-----------------------------------------
				/** print string_literals to output
					can be anything inside ""
				 */
				Pattern pPrintStr = Pattern.compile("print " + string + ";");
				Matcher mPrintStr = pPrintStr.matcher(line);	
				if(mPrintStr.find()) {
					System.out.println(mPrintStr.group(0));		
					w.write("System.out.println(\"" + mPrintStr.group(1) + "\");\n");
					continue;
				}
						
				// print variables to output
				Pattern pPrintVar = Pattern.compile("print " + variable + ";");
				Matcher mPrintVar = pPrintVar.matcher(line);
					
				if(mPrintVar.find()) {
					System.out.println(mPrintVar.group(0));		
					w.write("System.out.println(" + mPrintVar.group(1) + ");\n");
					continue;
				}
					
				
				//-----------------------------------------
				// comments: can be anything after double dashes (//)
				Pattern pCmt = Pattern.compile("//(.*)");
				Matcher mCmt = pCmt.matcher(line);
					
				if(mCmt.find()) {
					System.out.println(mCmt.group(0));		
					w.write("//" + mCmt.group(1) + "\n");
					continue;
				}
				
				
				//-----------------------------------------
				/** Variable assignments: 
					<variable_assign> ::= int|real|string|boolean <variable> = 
					<i_root>|<boolean>|<string_literal>|<expr_intr>|<expr_bool>|<arr_access>|<arr_expr>;|
					int|real|string|boolean[] <variable> = 
					<i_root>|<boolean>|<string_literal>|<expr_intr>|<expr_bool>|<arr_access>|<arr_expr>;
				*/
				// TO BE FILLED
				
				
				//-----------------------------------------
				/** Conditionals:
					<conditional> ::= if <expr_bool> then <stmt>; | if <expr_bool> then <stmt> else <stmt>;
					<stmt> ::= <var> = <expr>; | return <expr>; | <variable_assign>;
					<expr> ::= <integer> | <real> | <boolean> | <expr_int> | <expr_bool> | <arr_expr> | <arr_access
				*/ 
				// TO BE FILLED
				
				 
				
				//-----------------------------------------
				/** Loops:
					loops
					<loop> ::= while <integer> {<stmt>} | while <expr_bool> {<stmt>}
				*/
				// TO BE FILLED

				
				
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
