
public class Program1{

public static void main(String[] args){
// program to find multiples x or y between 1 and m
int x = (int) args[0];
int y = (int) args[1];
int m = (int) args[2];
int count = 0;
int i = 1;
while (i <= m){
if (i % x == 0){
	count = count + 1;
}
if (i % y == 0){
	count = count + 1;
}
if (i % x == 0 && i % y == 0){
	count = count - 1;
}
}
System.out.println(count);

}

}