package projectParser;
import edu.princeton.cs.algs4.IndexMaxPQ;

//DOES ALL WITH TFIDF MATRIX
public class SimilarityTFIDF {

	private DataParser dp;
	private CourseMatrix matrix;
		
	// holds the Jaccard similarity measure of all
	// courses based on terms arrays
	public IndexMaxPQ[] jaccardSim;
	
	//  holds the cosine similarity of all courses 
	//  based on terms arrays
	public IndexMaxPQ[] cosineSim;
	
	//holds the .4.6  distance of all courses
	public IndexMaxPQ[] bhatSim;
	
	
	public SimilarityTFIDF(DataParser dp, CourseMatrix matrix) {
		this.dp = dp;
		this.matrix = matrix;
		
		//this.jaccardSim = makeJaccardTable();
		//this.cosineSim = makeCosineTable();
		this.bhatSim = makeBhatTable();
	}
	
	
	public IndexMaxPQ[] makeJaccardTable() {
		IndexMaxPQ[] jaccard = new IndexMaxPQ[dp.getNumCourses()];
		
		System.out.println("Jaccard");
		int a = matrix.courseIdIndex.get(10437);
		//for(int i = 0; i < jaccard.length; i++) {
			IndexMaxPQ<Double> add = new IndexMaxPQ<Double>(dp.getNumCourses());
			for (int j = 0; j < jaccard.length; j++) {
				int id1 = matrix.courseIndexId.get(a);
				int id2 = matrix.courseIndexId.get(j);
				double jac = jaccardSimilarityTerms(id1, id2);
				double jacTokens = jaccardSimilarityTokens(id1, id2);
				double returned = .4*jacTokens+.6*jac;
				System.out.println(j + " " + returned );
				add.insert(j, returned);
			}
			jaccard[a] = add;
		//}
		return jaccard;
	}
	
	public IndexMaxPQ[] makeCosineTable() {
		IndexMaxPQ[] cosine = new IndexMaxPQ[dp.getNumCourses()];
		
		System.out.println("Cosine");
		int a = matrix.courseIdIndex.get(10437);
		//for(int i = 0; i < cosine.length; i++) {
			IndexMaxPQ<Double> add = new IndexMaxPQ<Double>(dp.getNumCourses());
			for (int j = 0; j < cosine.length; j++) {
				int id1 = matrix.courseIndexId.get(a);
				int id2 = matrix.courseIndexId.get(j);				
				double cos = cosineSimilarityTerms(id1, id2);
				double cosTokens = cosineSimilarityTokens(id1, id2);
				double returned = .4*cosTokens + .6*cos;
				System.out.println(j + " " + returned );
				add.insert(j, returned );
				
			}
			cosine[a] = add;
		//}
		return cosine;
	}

	public IndexMaxPQ[] makeBhatTable() {
		IndexMaxPQ[] bhat = new IndexMaxPQ[dp.getNumCourses()];
		
		System.out.println("Bhat");
		int a = matrix.courseIdIndex.get(10437);
		//for(int i = 0; i < bhat.length; i++) {
			IndexMaxPQ<Double> add = new IndexMaxPQ<Double>(dp.getNumCourses());
			
			double sum = 0;
			
			for (int j = 0; j < bhat.length; j++) {
				int id1 = matrix.courseIndexId.get(a);
				int id2 = matrix.courseIndexId.get(j);				
				double bhatTerms = bhatDistanceTerms(id1, id2);
				double bhatTokens = bhatDistanceTokens(id1, id2);
				double returned = (.4*bhatTokens + .6*bhatTerms);
				System.out.println(j + " " + returned );
				add.insert(j, returned);
				
			}
			bhat[a] = add;
		//}
		return bhat;
	}
	

	public double bhatDistanceTerms(int courseID1, int courseID2) {
		int index1 = matrix.courseIdIndex.get(courseID1);
		int index2 = matrix.courseIdIndex.get(courseID2);
		
		double norm1 = getNorm(index1);
		double norm2 = getNorm(index2);
		
		double sum = 0;
		
		for (int i = 0; i < dp.getNumTerms(); i++) {
			double a = matrix.tfidf[i][index1]/norm1;
			double b = matrix.tfidf[i][index2]/norm2;
			sum += Math.sqrt((a)*(b));

		}
		
		return sum;
		
	}
	
	public double bhatDistanceTokens(int courseID1, int courseID2) {
		
		double[] simArray1 = new double[3];
		double[] simArray2 = new double[3];
		
		String deptCode1 = dp.getDeptCode(courseID1);
		String deptCode2 = dp.getDeptCode(courseID2);
		
		String catnum1 = dp.getCatalogNum(courseID1);
		String catnum2 = dp.getCatalogNum(courseID2);
		
		String typename1 = dp.getTypename(courseID1);
		String typename2 = dp.getTypename(courseID2);

		if (deptCode1.equals(deptCode2)) {
			simArray1[0] = 1;
			simArray2[0] = 1;
		}
		else {
			simArray1[0] = 0;
			simArray2[0] = 1;
		}
		
		if (catnum1.charAt(0) == (catnum2.charAt(0))) {
			simArray1[1] = 1;
			simArray2[1] = 1;
		}
		else {
			simArray1[1] = 0;
			simArray2[1] = 1;
		}
		
		if (typename1.equals(typename2)) {
			simArray1[2] = 1;
			simArray2[2] = 1;
		}
		else {
			simArray1[2] = 0;
			simArray2[2] = 1;
		}
		
		int sum = 0;
		for (int i = 0; i < 3; i++) {
			double a = simArray1[i];
			double b = simArray2[i];
			sum += Math.sqrt((a)*(b));
		}
		
		return sum;
	}
	
	public double getNorm(int index) {
		double sum = 0.0;
		
		for (int i = 0; i < dp.getNumTerms(); i++) {
			sum += matrix.tfidf[i][index]*matrix.tfidf[i][index];
		}
		
		sum = Math.sqrt(sum);
		return sum;
		
	}
	
	
	/* returns cosine similarity of terms of two courses */
	public double cosineSimilarityTerms(int courseID1, int courseID2) {
		
		int index1 = matrix.courseIdIndex.get(courseID1);
		int index2 = matrix.courseIdIndex.get(courseID2);
		
		double norm1 = getNorm(index1);
		double norm2 = getNorm(index2);
		
		double sum = 0;
		double cosSim;
		double course1sum = 0;
		double course2sum = 0;
		
		for (int i = 0; i < dp.getNumTerms(); i++) {
			double a = matrix.tfidf[i][index1]/norm1;
			double b = matrix.tfidf[i][index2]/norm2;
			sum += (a)*(b);
			course1sum += a * a;
			course2sum += b * b;
		}
		
		course1sum = Math.sqrt(course1sum);
		course2sum = Math.sqrt(course2sum);
		
		cosSim = sum/(course1sum*course2sum);
		
		return cosSim;
	}
	
	/* returns Jaccard similarity of terms of two courses */
	public double jaccardSimilarityTerms(int courseID1, int courseID2) {
		
		int index1 = matrix.courseIdIndex.get(courseID1);
		int index2 = matrix.courseIdIndex.get(courseID2);
		
		int intersection = 0;
		int union = 0;
		double jaccard;
		
		for (int i = 0; i < dp.getNumTerms(); i++) {
			if (matrix.tfidf[i][index1] > 0 && matrix.tfidf[i][index2] > 0) {
				intersection++;
			}
			if (matrix.tfidf[i][index1] > 0 || matrix.tfidf[i][index2] > 0) {
				union++;
			}
		}
		if (union == 0) {
			return 0;
		}
		jaccard = (double)intersection / (double)union;
		return jaccard;
	}

	/* specialty similarity function for professor, dept. code, type of course */
	public double cosineSimilarityTokens(int courseID1, int courseID2) {
		double[] simArray1 = new double[3];
		double[] simArray2 = new double[3];
		String deptCode1 = dp.getDeptCode(courseID1);
		String deptCode2 = dp.getDeptCode(courseID2);
		
		String catnum1 = dp.getCatalogNum(courseID1);
		String catnum2 = dp.getCatalogNum(courseID2);
		
		String typename1 = dp.getTypename(courseID1);
		String typename2 = dp.getTypename(courseID2);

		if (deptCode1.equals(deptCode2)) {
			simArray1[0] = 1;
			simArray2[0] = 1;
		}
		else {
			simArray1[0] = 0;
			simArray2[0] = 1;
		}
		
		if (catnum1.charAt(0) == (catnum2.charAt(0))) {
			simArray1[1] = 1;
			simArray2[1] = 1;
		}
		else {
			simArray1[1] = 0;
			simArray2[1] = 1;
		}
		
		if (typename1.equals(typename2)) {
			simArray1[2] = 1;
			simArray2[2] = 1;
		}
		else {
			simArray1[2] = 0;
			simArray2[2] = 1;
		}
		
		double sum = 0;
		double cosSim;
		double course1sum = 0;
		double course2sum = 0;
		
		for (int i = 0; i < 3; i++) {
			sum += simArray1[i]*simArray2[i];
			course1sum += simArray1[i]*simArray1[i];
			course2sum += simArray2[i]*simArray2[i];
		}
		
		course1sum = Math.sqrt(course1sum);
		course2sum = Math.sqrt(course2sum);
		
		if (course1sum == 0 || course2sum == 0) return 0;
		cosSim = sum/(course1sum*course2sum);
		
		return cosSim;

	}
	
	/* specialty similarity function for professor, dept. code, type of course */
	public double jaccardSimilarityTokens(int courseID1, int courseID2) {

		double[] simArray1 = new double[3];
		double[] simArray2 = new double[3];
		String deptCode1 = dp.getDeptCode(courseID1);
		String deptCode2 = dp.getDeptCode(courseID2);
		
		String catnum1 = dp.getCatalogNum(courseID1);
		String catnum2 = dp.getCatalogNum(courseID2);
		
		String typename1 = dp.getTypename(courseID1);
		String typename2 = dp.getTypename(courseID2);

		if (deptCode1.equals(deptCode2)) {
			simArray1[0] = 1;
			simArray2[0] = 1;
		}
		else {
			simArray1[0] = 0;
			simArray2[0] = 1;
		}
		
		if (catnum1.charAt(0) == (catnum2.charAt(0))) {
			simArray1[1] = 1;
			simArray2[1] = 1;
		}
		else {
			simArray1[1] = 0;
			simArray2[1] = 1;
		}
		
		if (typename1.equals(typename2)) {
			simArray1[2] = 1;
			simArray2[2] = 1;
		}
		else {
			simArray1[2] = 0;
			simArray2[2] = 1;
		}
		
		int intersection = 0;
		int union = 0;
		double jaccard;
		
		for (int i = 0; i < 3; i++) {
			if (simArray1[i] > 0 && simArray2[i] > 0) {
				intersection++;
			}
			if (simArray1[i] > 0 || simArray2[i] > 0) {
				union++;
			}
		}
		if (union == 0) return 0;
		jaccard = (double)intersection / (double)union;
		return jaccard;
		
	} 
	
	public  void printSims(int index) {
		
		for (int i = 0; i < 15; i++) {
			System.out.println("Cosine " + i + ": " + cosineSim[index].delMax());
		}
		for (int j = 0; j < 15; j++) {
			System.out.println("Jaccard " + j + ": " + jaccardSim[index].delMax());
		}
		for(int z = 0; z < 15; z++) {
			System.out.println("Bhat " + z + ": " + bhatSim[index].delMax());
		}	
		
	}
	
	/* build Similarity data type */
		public static void main(String[] args) {
			DataParser dp = new DataParser();
			CourseMatrix matrix = new CourseMatrix(dp);
			//System.out.println(matrix.courseIdIndex.get(10437));
			
			SimilarityTFIDF sim = new SimilarityTFIDF(dp, matrix);
			
			//Muslims and the Qu'ran
			/*System.out.println("Muslims and the Qu'ran");
			//sim.printSims(matrix.courseIdIndex.get(10437));
			System.out.println();*/
			
			/*//General Computer Science
			System.out.println("General Computer Science");
			sim.printSims(matrix.courseIdIndex.get(2051));
			System.out.println();
			
			//Networks Friends Money and Bytes
			System.out.println("Networks Friends Money and Bytes");
			sim.printSims(matrix.courseIdIndex.get(11724));
			System.out.println();
			
			//Beginner's German
			System.out.println("German");
			sim.printSims(matrix.courseIdIndex.get(3137));
			System.out.println();
			
			//Life On Earth
			System.out.println("Life On Earth");
			sim.printSims(matrix.courseIdIndex.get(1521));
			System.out.println();
			
			//Social Philosophy
			System.out.println("Social Philosophy");
			sim.printSims(matrix.courseIdIndex.get(13290));
			System.out.println();
			
			//Afro-Asian masculinities
			System.out.println("Afro-Asian masculinities");
			sim.printSims(matrix.courseIdIndex.get(13238));
			System.out.println();
			
			//Selected Topics Condensed Matter Theory
			System.out.println("Selected Topics Condensed Matter Theory");
			sim.printSims(matrix.courseIdIndex.get(13179));
			System.out.println();
			
			//Digital Photography
			System.out.println("Digital Photography");
			sim.printSims(matrix.courseIdIndex.get(8907));
			System.out.println();
			
			//Black to the Future
			System.out.println("Black to the Future");
			sim.printSims(matrix.courseIdIndex.get(13009));
			System.out.println();*/

		}
		
}


