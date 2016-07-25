package projectParser;
import edu.princeton.cs.algs4.StdIn;

public class Decoder {
	public static void main(String[] args) {
		
		DataParser dp = new DataParser();
		System.out.println("abby");
		CourseMatrix matrix = new CourseMatrix(dp);
		System.out.println("ambi");
		
		int count = 0;
		int count2 =0;

		
		while(!(StdIn.isEmpty()))
		{
			int courseIndex = StdIn.readInt();
			int courseId = matrix.courseIndexId.get(courseIndex);
			String courseDep = dp.getDeptCode(courseId);
			String catNum = dp.getCatalogNum(courseId);
			String courseName = dp.getTitle(courseId);
			System.out.println(courseDep + " " + catNum + ": "  + courseName);
			count++;
			count2++;
			
			if (count == 15) {
				System.out.println("--------------------");
				count = 0;
			}
			
			if(count2 == 45) {
				System.out.println("*****************");
				count2 = 0;
			}
			
			
			
	
		}
	}
}
