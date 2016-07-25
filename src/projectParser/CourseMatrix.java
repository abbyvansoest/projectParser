package projectParser;

import edu.princeton.cs.algs4.ST;

/* In order to sleep, we must make this matrix. Thank you. */
public class CourseMatrix {
	public int[][] termByCourse;
	private int numTerms;
	private int numCourses;
	public double[][] tfidf;
	
	
	// give courseID, receive index in array
	public ST<Integer, Integer> courseIdIndex;
	public ST<Integer, Integer> courseIndexId;

	//construct a courseMatrix from a DataParser dp
	public CourseMatrix(DataParser dp) {
		numTerms = dp.getNumTerms();
		numCourses = dp.getNumCourses();
		//initialize termByCourse matrix
		termByCourse = new int[numTerms][numCourses];
		courseIdIndex = new ST<Integer, Integer>();
		courseIndexId = new ST<Integer, Integer>();

		//System.out.println("From matrix: " + dp.getNumTerms());
		//System.out.println("From matrix: " + dp.getNumCourses());

			    

		//initialize to zero
		for(int i = 0; i < numCourses - 1; i++) {
			for(int j = 0; j < numTerms - 1; j++) {
				termByCourse[j][i] = 0;
			}
		}
		
		String[] terms;
		
		//for each course, put in frequency of terms in description+title into matrix
		int index;
		int course = 0;
		for (Integer courseID: dp.toBeVector) {
			if (courseID == 13014) System.out.println("put");
			courseIdIndex.put(courseID, course);
			courseIndexId.put(course, courseID);
			//System.out.println(courseID);
			//System.out.println("course + " + course);
			terms = dp.toBeVector.get(courseID);
			for (int j = 0; j < terms.length; j++) {
				index = dp.wordIndex.get(terms[j]);
				//System.out.println("first " + termByCourse[index][course]);
				termByCourse[index][course]++;
				//System.out.println("second " + termByCourse[index][course]);
			}
			course++;
		}
		
		//make tfidf matrix
		tfidf = makeTfIdf(dp);
	}
	
	/* calculate tfidf for each course and store in table */
	public double[][] makeTfIdf(DataParser dp) {
		double[][] tfidfTable = new double[numTerms][numCourses];
		
		for (Integer courseID: dp.toBeVector) {
			//System.out.println(courseID);
			int i = courseIdIndex.get(courseID);
			int numTermsInDoc = dp.toBeVector.get(courseID).length;
			
			for (int j = 0; j < numTerms; j++) {
				//for TF
				int appears = termByCourse[j][i];
				
				//for IDF
				String s = dp.indexToWord.get(j);
				int numDocsWithTerm = dp.wordTable.get(s);
				
				double TF = (double) appears/ (double)numTermsInDoc;
				double IDF = Math.log(numCourses/numDocsWithTerm);
				
				tfidfTable[j][i] = TF*IDF;
				
			}
		}
		
		return tfidfTable;
	}
	
	/* return the term vector of the selected course */
	public int[] getCourseTermsVector(int courseID) {
		
		int[] courseTermsVector = new int[numTerms];
		
		for (int i = 0; i < numTerms; i++) {
			courseTermsVector[i] = termByCourse[i][courseID];
		}
	
		return courseTermsVector;
	}

	// create courseMatrix 
	public static void main(String[] args) {
		DataParser dp = new DataParser();
		CourseMatrix matrix = new CourseMatrix(dp);
	}

}