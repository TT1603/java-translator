
public class Program1{

public static void main(String[] args){
// program to find multiples x or y between 1 and m
x = (int) args[0];
y = (int) args[1];
m = (int) args[2];
count = 0;
i = 1;
while (i <= m){
if (i % x == 0){
	count = count + 1;
}
else if (i % y == 0){
	count = count + 1;
}
else if (i % x == 0 && i % y == 0){
	count = count - 1;
}
}
System.out.println(count);
}
}