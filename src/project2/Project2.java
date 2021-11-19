package project2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
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
		    
		    
		    // translate the input file to Java and write to new output file
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
	
	
	/** basic components: integer, real, boolean
	variable names, string literals
	 */
	static Pattern integer = Pattern.compile("(\\d+)");
	static Pattern bool = Pattern.compile("true|false");
	static Pattern variable = Pattern.compile("([a-z]+)");
	static Pattern string = Pattern.compile("\\\"(.*)\\\"");

	static boolean inIfElse = false;

	public static void parseFile(FileWriter w, BufferedReader br) {
		try {
			String line;  
			while((line = br.readLine()) != null) {	
				
				// pre-process the line
				line = line.strip();
				if (line.length() == 0) {
					continue;
				}
				if (line.charAt(line.length()-1) == ';') {
					line = line.substring(0, line.length()-1);
				}
				
				
				// command-line arguments: 
				// <command_line> ::= <variable> = args<integer>
				Pattern pArgs1; Matcher mArgs1;
				pArgs1 = Pattern.compile("var " + variable + " = arg" + integer);
				mArgs1 = pArgs1.matcher(line);
				if(mArgs1.find()) {						
					w.write("int " + mArgs1.group(1) + " = Integer.valueOf(args[" + mArgs1.group(2) + "]);\n");
					continue;
				}
				
				pArgs1 = Pattern.compile(variable + " = arg" + integer);
				mArgs1 = pArgs1.matcher(line);
				if(mArgs1.find()) {						
					w.write(mArgs1.group(1) + " = (int) args[" + mArgs1.group(2) + "];\n");
					continue;
				}		
				
				// comments: can be anything after double dashes (//)
				Pattern pCmt = Pattern.compile("//(.*)");
				Matcher mCmt = pCmt.matcher(line);				
				if(mCmt.find()) {
					w.write("//" + mCmt.group(1) + "\n");
					continue;
				}
				
				// CONDITIONALS (IF-ELSE)
				// <conditional> ::= if <expr_bool> then <stmt> end. | else if <expr_bool> then <stmt> end.
				// | if <expr_bool> then <stmt> else <stmt> end.
				// | else if <expr_bool> then <stmt> else <stmt> end.
				// | else <stmt> end.
				// <stmt> ::= <var> = <expr> | return <expr> | <variable_assign>
				// <expr> ::= <integer> | <boolean> | <expr_int> | <expr_bool> 				
				
				Pattern p1; Matcher m1;
				
				p1 = Pattern.compile("else if (.*) then (.*) else (.*) end.");
				m1 = p1.matcher(line);	
				if(m1.find()) {
					throw new Exception("Wrong conditional " + m1.group(0));
				}
				p1 = Pattern.compile("if (.*) then (.*) else (.*) end.");
				m1 = p1.matcher(line);	
				if(m1.find()) {
					throw new Exception("Wrong conditional " + m1.group(0));
				}
				p1 = Pattern.compile("else (.*) end.");
				m1 = p1.matcher(line);	
				if(m1.find()) {
					throw new Exception("Wrong conditional " + m1.group(0));
				}
				
				p1 = Pattern.compile("else if (.*) then (.*) else (.*)");
				m1 = p1.matcher(line);			
				if(m1.find()) {
					if (!parseBool(m1.group(1))) {
						throw new Exception("Wrong conditional " + m1.group(1));
				    }
					w.write("else if (" + m1.group(1) + "){\n\t");
					
					if (!parseStmt(m1.group(2), w)) {
						throw new Exception("Wrong conditional " + m1.group(2));
					}		
					w.write("}else{\n\t");
					
					if (!parseStmt(m1.group(3), w)) {
						throw new Exception("Wrong conditional " + m1.group(3));
					}		
					w.write("}\n");
					continue;
				}			
				
				p1 = Pattern.compile("if (.*) then (.*) else (.*)");
				m1 = p1.matcher(line);				
				if(m1.find()) {
					if (!parseBool(m1.group(1))) {
						throw new Exception("Wrong conditional " + m1.group(1));
				    }
					w.write("if (" + m1.group(1) + "){\n\t");
					
					if (!parseStmt(m1.group(2), w)) {
						throw new Exception("Wrong conditional " + m1.group(2));
					}		
					w.write("}else{\n\t");
					
					if (!parseStmt(m1.group(3), w)) {
						throw new Exception("Wrong conditional " + m1.group(3));
					}		
					w.write("}\n");
					continue;
				}	
							
				p1 = Pattern.compile("else if (.*) then (.*)");
				m1 = p1.matcher(line);			
				if(m1.find()) {
					if (!parseBool(m1.group(1))) {
						throw new Exception("Wrong conditional " + m1.group(1));
				    }
					w.write("else if (" + m1.group(1) + "){\n\t");
					
					if (!parseStmt(m1.group(2), w)) {
						throw new Exception("Wrong conditional " + m1.group(2));
					}		
					w.write("}\n");
					continue;
				}	
							
				p1 = Pattern.compile("if (.*) then (.*)");
				m1 = p1.matcher(line);			
				if(m1.find()) {
					if (!parseBool(m1.group(1))) {
						throw new Exception("Wrong conditional " + m1.group(1));
				    }
					w.write("if (" + m1.group(1) + "){\n\t");
					
					if (!parseStmt(m1.group(2), w)) {
						throw new Exception("Wrong conditional " + m1.group(2));
					}		
					w.write("}\n");
					continue;
				}
							
				p1 = Pattern.compile("else if (.*) then");
				m1 = p1.matcher(line);			
				if(m1.find()) {
					inIfElse = true;
					w.write("else if (" + m1.group(1) + "){\n\t");
					continue;
				}
				
				p1 = Pattern.compile("if (.*) then");
				m1 = p1.matcher(line);			
				if(m1.find()) {
					inIfElse = true;
					w.write("if (" + m1.group(1) + "){\n\t");
					continue;
				}
				
				p1 = Pattern.compile("else (.*)");
				m1 = p1.matcher(line);
				if(m1.find()) {
					w.write("else{\n\t");
					if (!parseStmt(m1.group(1), w)) {
						throw new Exception("Wrong conditional " + m1.group(1));
					}		
					w.write("}");			
					continue;
				}
				
				p1 = Pattern.compile("else");
				m1 = p1.matcher(line);			
				if(m1.find()) {
					inIfElse = true;
					w.write("else{\n\t");
					continue;
				}

				
				// LOOPS (WHILE)
				// <loop> ::=
				// while <expr_bool>: 
				// <stmt>
				// end.
				Pattern p2; Matcher m2;
				p2 = Pattern.compile("while (.*):");
				m2 = p2.matcher(line);			
				if(m2.find()) {
					if (!parseBool(m2.group(1))) {
						throw new Exception("Wrong loop condition " + m2.group(1));
					}
					w.write("while (" + m2.group(1) + "){\n");
					continue;
				}	
				p2 = Pattern.compile("end.");
				m2 = p2.matcher(line);
				if(m2.find()) {
					if (line.equals("end.")) {
						w.write("}\n");
						continue;
					}else {
						throw new Exception("Wrong statement " + line);
					}
				}	
			
				
				if (varAssg(line, w)) {
					continue;
				}
				
				if (printStmt(line, w)) {
					continue;
				}
				
				if (parseStmt(line, w)) {
					continue;
				}
			
				// cannot find a pattern
				System.out.print("ERROR: " + line + "\n");
				throw new Exception("Line not in grammar " + line + "\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	
	// Assign variable. 
	// Return true if can parse according to grammar and false otherwise
	public static boolean varAssg(final String str, FileWriter w) {
		//-----------------------------------------
		// Variable assignment
		// var <variable> ::= <expr>
		// <expr> ::= <integer> | <variable> | <boolean> | <string_literal> |<expr_int> | <expr_bool> 
		Pattern p = Pattern.compile("var " + variable + " = " + "(.*)");
		Matcher m = p.matcher(str);
		if(m.find()) {
			if (isExpr(m.group(2))) {
				try {
					w.write("int " + m.group(1) + " = " + m.group(2) + ";\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}

	
	
	// Parse integer expression. 
	// Return true if can parse according to grammar and false otherwise
	public static boolean parseInt(final String str) {
	    return new Object() {
	    	int pos = -1;
	        int curChar;

	        void nextChar() {
	        	if (++pos < str.length()) {
	        		curChar =  str.charAt(pos);
	        	}
	        	else {
	        		curChar =  -1;
	        	}
	        }

	        boolean eat(int charToEat) {
	            while (curChar == ' ') {
	            	nextChar();
	            }
	            if (curChar == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        boolean parse() {
	            nextChar();
	            boolean x = parseExpression();
	            if (pos < str.length()) {
	            	return false;
	            }
	            return x;
	        }

	        /**
			<expr_int> ::= <expr_int> + <mul_expr> | <expr_int> - <mul_expr> | <mul_expr> 
			<mul_expr> ::= <mul_expr> * <neg_expr> | <mul_expr> / <neg_expr> | <mul_expr> % <neg_expr> | <neg_expr>
			<neg_expr> ::= - <i_root> | <i_root> | (<i_root>) 
			<i_root> ::= <integer> | <variable> | sqrt(<expr_int>)
	         */

	        boolean parseExpression() {
	            boolean x = parseTerm();
	            for (;;) {
	                if (eat('+')) {
	                	x = parseTerm(); // addition
	                }
	                else if (eat('-')) {
	                	x = parseTerm(); // subtraction
	                }
	                else{
	                	return x;
	                }
	            }
	        }

	        boolean parseTerm() {
	            boolean x = parseFactor();
	            for (;;) {
	                if (eat('*')) {
	                	x = parseFactor(); // multiplication
	                }
	                else if (eat('/')) {
	                	x = parseFactor(); // division
	                }
	                else if (eat('%')) {
	                	x = parseFactor(); // modulo
	                }
	                else return x;
	            }
	        }

	        boolean parseFactor() {
	            if (eat('-')) {
	            	return parseFactor(); // unary minus
	            }

	            boolean x = true;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                if (!eat(')')) {
	                	return false;
	                }
	            } else if ((curChar >= '0' && curChar <= '9') || curChar >= 'a' && curChar <= 'z') { // numbers
	                while ((curChar >= '0' && curChar <= '9') || curChar >= 'a' && curChar <= 'z') {
	                	nextChar();
	                }
	            } else {
	                return false;
	            }

	            return x;
	        }
	    }.parse();
	}
	
	
	
	// Parse boolean expression. 
	// Return true if can parse according to grammar and false otherwise
	public static boolean parseBool(final String str) {
	    return new Object() {
	        int pos = -1;
	        int curChar;

	        void nextChar() {
	        	if (++pos < str.length()) {
	        		curChar =  str.charAt(pos);
	        	}
	        	else {
	        		curChar =  -1;
	        	}
	        }

	        boolean eat(int charToEat) {
	            while (curChar == ' ') {
	            	nextChar();
	            }
	            if (curChar == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        boolean parse() {
	            nextChar();
	            boolean x = parseExpression();
	            return x;
	        }

	        /**
			<expr_bool> ::= <expr_bool> || <and_expr> | <and_expr>
			<and_expr> ::= <and_expr> && <not_expr> | <not_expr>
			<not_expr> ::= !<b_root> | <b_root> 
			<b_root> ::= true | false | <expr> <operator> <expr> 
			<operator> ::= < | > | = | <= | >= | ==
	         */

	        boolean parseExpression() {
	            boolean x = parseAnd();
	            for (;;) {
	                if (eat('|') && eat('|')) {
	                	x = parseAnd(); 
	                }
	                else {
	                	return x;
	                }
	            }
	        }
	        boolean parseAnd() {
	            boolean x = parseOr();
	            for (;;) {
	                if (eat('&') && eat('&')) {
	                	x = parseOr();
	                }
	                else {
	                	return x;
	                }
	            }
	        }   
	        boolean parseOr() {
	            boolean x = parseNot();
	            for (;;) {
	                if (eat('!')) {
	                	x = parseNot();
	                }
	                else {
	                	return x;
	                }
	            }
	        }
	        boolean parseNot() {
	            boolean x = parseRoot();
	            for (;;) {
	                if (eat('!')) {
	                	x = parseRoot();
	                }
	                else {
	                	return x;
	                }
	            }
	        }	        
	        boolean parseRoot() {     	
	            boolean x = true;
	            if (eat('(')) { 
	                x = parseExpression();
	                if (!eat(')')) {
	                	return false;
	                }
	            } else if ((curChar >= '0' && curChar <= '9') || curChar >= 'a' && curChar <= 'z' || curChar == '<' || curChar == '>' || curChar == '=') { 
	                while ((curChar >= '0' && curChar <= '9') || curChar >= 'a' && curChar <= 'z' || curChar == '<' || curChar == '>' || curChar == '=') {
	                	nextChar();
	                }
	            } else {
	                return false;
	            }
	            return x;
	        }
	    }.parse();
	}
	
	
	
	// Parse a statement: <stmt> ::= <var> = <expr> | return <expr> | <variable_assign> 
	// Return true if can parse according to grammar and false otherwise
	public static boolean parseStmt(final String str, FileWriter w) {
		/**
		<stmt> ::= <var> = <expr> | return <expr> | <variable_assign> | <print>
		 */
		try {
			if (varAssg(str, w)) {
				return true;
			}
			if (printStmt(str, w)) {
				return true;
			}
		
			Pattern p; Matcher m;
			p = Pattern.compile(variable + " = " + "(.*)");
			m = p.matcher(str);	
			if(m.find()) {
				if(isExpr(m.group(2))) {
					w.write(m.group(1) + " = " + m.group(2) + ";\n");
					return true;
				}
				return false;
			}
			p = Pattern.compile("return (.*)");
			m = p.matcher(str);	
			if(m.find()) {
				if(isExpr(m.group(1))) {
					w.write("return " + m.group(1) + ";\n");
					return true;
				}
				return false;
			}
			p = Pattern.compile("return");
			m = p.matcher(str);	
			if(m.find()) {
				w.write("return;\n");
				return true;
			}	 
		}catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	// print string_literals or variable to output
	// strings can be anything inside ""	
	public static boolean printStmt(final String str, FileWriter w) {
		try {
			Pattern pPrint; Matcher mPrint;
			pPrint = Pattern.compile("print " + string);
			mPrint = pPrint.matcher(str);	
			if(mPrint.find()) {
				w.write("System.out.println(\"" + mPrint.group(1) + "\");\n");
				return true;
			}
			
			pPrint = Pattern.compile("print " + variable);
			mPrint = pPrint.matcher(str);
			if(mPrint.find()) {
				w.write("System.out.println(" + mPrint.group(1) + ");\n");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	// Return true if string is an expression and false otherwise
	// <expr> := <integer> | <variable> | <boolean> | <string_literal> |<expr_int> | <expr_bool> 
	public static boolean isExpr(final String str) {
		if (integer.matcher(str) != null || bool.matcher(str) != null ||
				variable.matcher(str) != null  || string.matcher(str) != null ||
				parseInt(str) || parseBool(str)) {
			return true;
		}else {
			return false;
		}
	}

	
	
	// Initiate code for Java program
	public static void initialCode(FileWriter w, String name) {
		try {
			w.write("\npublic class " + name + "{\n");
			w.write("\npublic static void main(String[] args){\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
