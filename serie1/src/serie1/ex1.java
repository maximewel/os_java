package serie1;

public class ex1 {
	//parameters
	static String name;
	
	public static void main(String[] args) {
		String s = new String("SomeName");
		System.out.println(compare(s,name));
		System.out.println("compare(name,s) is " + compare(s, name));
	}
	
	static boolean compare(String a, String b) {
		return a.equals(b);
	}

}
