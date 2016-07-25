package projectParser;

import edu.princeton.cs.algs4.IndexMaxPQ;

public class Similarity {
	
	private DataParser dp;
	private CourseMatrix matrix;
	
	// holds the Jaccard similarity measure of all
	// courses based on terms arrays
	public IndexMaxPQ[] jaccardSim;
	//  holds the cosine similarity of al courses 
	//  based on terms arrays
	public IndexMaxPQ[] cosineSim;
	
	public Similarity(DataParser dp, CourseMatrix matrix) {
		this.dp = dp;
		this.matrix = matrix;
		
		this.jaccardSim = makeJaccardTable();
		this.cosineSim = makeCosineTable();
	}
	
	
	public IndexMaxPQ[] makeJaccardTable() {
		IndexMaxPQ[] jaccard = new IndexMaxPQ[dp.getNumCourses()];
		
		int a = matrix.courseIdIndex.get(010437);
		for(int i = 0; i < jaccard.length; i++) {
			IndexMaxPQ<Double> add = new IndexMaxPQ<Double>(dp.getNumCourses());
			for (int j = 0; j < jaccard.length; j++) {
				int id1 = matrix.courseIndexId.get(i);
				int id2 = matrix.courseIndexId.get(j);
				double jac = jaccardSimilarityTerms(id1, id2);
				double jacTokens = jaccardSimilarityTokens(id1, id2);
				add.insert(j, (.4*jacTokens+.6*jac));
			}
			jaccard[i] = add;
		}
		return jaccard;
	}
	
	public IndexMaxPQ[] makeCosineTable() {
		IndexMaxPQ[] cosine = new IndexMaxPQ[dp.getNumCourses()];
		
		int a = matrix.courseIdIndex.get(010437);
		for(int i = 0; i < cosine.length; i++) {
			IndexMaxPQ<Double> add = new IndexMaxPQ<Double>(dp.getNumCourses());
			for (int j = 0; j < cosine.length; j++) {
				int id1 = matrix.courseIndexId.get(i);
				int id2 = matrix.courseIndexId.get(j);				
				double cos = cosineSimilarityTerms(id1, id2);
				double cosTokens = cosineSimilarityTokens(id1, id2);
				add.insert(j, (.4*cosTokens + .6*cos));
				
			}
			cosine[i] = add;
		}
		return cosine;
	}
	
	public IndexMaxPQ[] makeEuclidTable() {
		IndexMaxPQ[] euclid = new IndexMaxPQ[dp.getNumCourses()];
		
		int a = matrix.courseIdIndex.get(010437);
		for(int i = 0; i < euclid.length; i++) {
			IndexMaxPQ<Double> add = new IndexMaxPQ<Double>(dp.getNumCourses());
			for (int j = 0; j < euclid.length; j++) {
				int id1 = matrix.courseIndexId.get(i);
				int id2 = matrix.courseIndexId.get(j);				
				double euc = euclideanDistanceTerms(id1, id2);
				double eucTokens = euclideanDistanceTokens(id1, id2);
				add.insert(j, (.4*eucTokens + .6*euc));
				
			}
			euclid[i] = add;
		}
		return euclid;
	}
	
	public IndexMaxPQ[] makeBhatTable() {
		IndexMaxPQ[] bhat = new IndexMaxPQ[dp.getNumCourses()];
		
		int a = matrix.courseIdIndex.get(010437);
		for(int i = 0; i < bhat.length; i++) {
			IndexMaxPQ<Double> add = new IndexMaxPQ<Double>(dp.getNumCourses());
			for (int j = 0; j < bhat.length; j++) {
				int id1 = matrix.courseIndexId.get(i);
				int id2 = matrix.courseIndexId.get(j);				
				double bhatTerms = bhatDistanceTerms(id1, id2);
				double bhatTokens = bhatDistanceTokens(id1, id2);
				add.insert(j, (.4*bhatTokens + .6*bhatTerms));
				
			}
			bhat[i] = add;
		}
		return bhat;
	}
	
	public double bhatDistanceTerms(int courseID1, int courseID2) {
		int index1 = matrix.courseIdIndex.get(courseID1);
		int index2 = matrix.courseIdIndex.get(courseID2);
		
		double sum = 0;
		double eucDist;
		
		for (int i = 0; i < dp.getNumTerms(); i++) {
			double a = matrix.termByCourse[i][index1];
			double b = matrix.termByCourse[i][index2];
			sum += (a)*(b);

		}
		
		eucDist = Math.sqrt(sum);		
		return eucDist;
		
	}
	
	public double bhatDistanceTokens(int courseID1, int courseID2) {
		double eucDist;
		
		double[] simArray1 = new double[3];
		double[] simArray2 = new double[3];
		
		String deptCode1 = dp.getDeptCode(courseID1);
		String deptCode2 = dp.getDeptCode(courseID2);
		int catnum1 = Integer.parseInt(dp.getCatalogNum(courseID1));
		int catnum2 = Integer.parseInt(dp.getCatalogNum(courseID2));
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
		
		if (catnum1 / 100 == catnum2 / 100) {
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
			sum += (a)*(b);
		}
		
		eucDist = Math.sqrt(sum);		
		return eucDist;
	}
	
	public double euclideanDistanceTerms(int courseID1, int courseID2) {
		
		int index1 = matrix.courseIdIndex.get(courseID1);
		int index2 = matrix.courseIdIndex.get(courseID2);
		
		double sum = 0;
		double eucDist;
		
		for (int i = 0; i < dp.getNumTerms(); i++) {
			double a = matrix.termByCourse[i][index1];
			double b = matrix.termByCourse[i][index2];
			sum += (a-b)*(a-b);

		}
		
		eucDist = Math.sqrt(sum);		
		return eucDist;
		
	}
	
	public double euclideanDistanceTokens(int courseID1, int courseID2) {
		double eucDist;
		
		double[] simArray1 = new double[3];
		double[] simArray2 = new double[3];
		
		String deptCode1 = dp.getDeptCode(courseID1);
		String deptCode2 = dp.getDeptCode(courseID2);
		int catnum1 = Integer.parseInt(dp.getCatalogNum(courseID1));
		int catnum2 = Integer.parseInt(dp.getCatalogNum(courseID2));
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
		
		if (catnum1 / 100 == catnum2 / 100) {
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
			sum += (a-b)*(a-b);
		}
		
		eucDist = Math.sqrt(sum);		
		return eucDist;
	
	}
	
	public double getNorm(int index) {
		double sum = 0.0;
		
		for (int i = 0; i < dp.getNumTerms(); i++) {
			sum += matrix.termByCourse[i][index]*matrix.termByCourse[i][index];
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
			double a = matrix.termByCourse[i][index1]/norm1;
			double b = matrix.termByCourse[i][index2]/norm2;
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
			if (matrix.termByCourse[i][index1] > 0 && matrix.termByCourse[i][index2] > 0) {
				intersection++;
			}
			if (matrix.termByCourse[i][index1] > 0 || matrix.termByCourse[i][index2] > 0) {
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
		
		int catnum1 = Integer.parseInt(dp.getCatalogNum(courseID1));
		int catnum2 = Integer.parseInt(dp.getCatalogNum(courseID2));
		
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
		
		if (catnum1 / 100 == catnum2 / 100) {
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
		
		int catnum1 = Integer.parseInt(dp.getCatalogNum(courseID1));
		int catnum2 = Integer.parseInt(dp.getCatalogNum(courseID2));
		
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
		
		if (catnum1 / 100 == catnum2 / 100) {
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
				union++;
			}
			if (simArray1[i] > 0 || simArray2[i] > 0) {
				intersection++;
			}
		}
		if (union == 0) return 0;
		jaccard = (double)intersection / (double)union;
		return jaccard;
		
	} 
	
	/* build Similarity data type */
	public static void main(String[] args) {
		DataParser dp = new DataParser();
		CourseMatrix matrix = new CourseMatrix(dp);
		Similarity sim = new Similarity(dp, matrix);

	}
	
}
