
public class Program2{

public static void main(String[] args){
int x = Integer.valueOf(args[0]);
// Check if number is less than or equal to 1
if (x <= 1){
	System.out.println("not prime");
return;
}
// Check if number is 2
else if (x == 2){
	System.out.println("prime");
return;
}
// Check if n is a multiple of 2
else if (x % 2 == 0){
	System.out.println("not prime");
return;
}
// If not, then just check the odds
int i = 3;
while (i <= Math.sqrt(x)){
if (x % i == 0){
	System.out.println("not prime");
return;
}
else{
	i = i + 2;
}
}
System.out.println("prime");
}
}