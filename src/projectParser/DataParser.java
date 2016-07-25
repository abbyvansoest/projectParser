package projectParser;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import javax.xml.validation.*;
import edu.princeton.cs.algs4.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DataParser {
	
	//CourseID associated with CourseNode
	private ST<Integer, CourseNode> symTable;
	
	//all words encountered associated with number of courses appear in
	public ST<String, Integer> wordTable;
	
	//courseID associated with string description+title
	public ST<Integer, String[]> toBeVector;
	
	//word associated with index
	public ST<String, Integer> wordIndex;
	
	//index associated with word
	public ST<Integer, String> indexToWord;


	// A node that encapsulates the data associated with a course
	private class CourseNode {
		private int courseID;
		private String title;
		private String description;
		private String departmentCode;
		private String catalogNumber;
		private int emplid;
		private String typename;
		private int numTermsInDoc;
		
		//constructor for CourseNode
		public CourseNode(int courseID, String title, String description,
				String deptCode, String catalogNumber, int emplid, String typename)
		{
			this.courseID = courseID;
			this.title = title;
			this.description = description;
			this.departmentCode = deptCode;
			this.catalogNumber = catalogNumber;
			this.emplid = emplid;
			this.typename = typename;
			this.numTermsInDoc = 0;
		}
	}
	
	//Print all the fields of a CourseNode node
	public void printNode(CourseNode node) {
		System.out.println("CourseID: " + node.courseID);
		System.out.println("Title: " + node.title);
		System.out.println("Description: " +node.description);
		System.out.println("DepartmentCode: " + node.departmentCode);
		System.out.println("CatalogNumber: " + node.catalogNumber);
		System.out.println("Emplid: " + node.emplid);
		System.out.println("TypeName: " + node.typename);
		System.out.println("NumTermsInDoc: " + node.numTermsInDoc);
		System.out.println();
	}
	
	//constructor that reads in from a xml txt file, parses into various tables
	public DataParser() {
		//initialize symtables
		symTable = new ST<Integer, CourseNode>();
		toBeVector = new ST<Integer, String[]>();
		wordIndex = new ST<String, Integer>();
		indexToWord = new ST<Integer, String>();
		
		try {
			//build DOM model
			File inputFile = new File("singleterm.txt");
	        DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        
	        //create NodeList for all subjects, are iterate through them
	        NodeList nListSubject = doc.getElementsByTagName("subject");
	        for (int j = 0; j < nListSubject.getLength(); j++) {
	        	
	        	//find jth subject node, if it is an element
	        	Node nNodeSubject = nListSubject.item(j);
	        	if (nNodeSubject.getNodeType() == Node.ELEMENT_NODE) {
	        		Element eElement = (Element) nNodeSubject;
	        		//save subject code
	        		String code = eElement.getElementsByTagName("code").item(0).getTextContent();	
		        
	        		//create nodelist of courses underneath given subject and iterate through them
		        	NodeList nListCourse = eElement.getElementsByTagName("course");	
		        	
					for( int i = 0; i < nListCourse.getLength(); i++) {
						
						//find ith course node, if it is an element
						Node nNodeCourse = nListCourse.item(i);
						if (nNodeCourse.getNodeType() == Node.ELEMENT_NODE) {
							Element eNextElement = (Element) nNodeCourse;
				            //find list of instructors of course
							NodeList nListInstructors = eNextElement.getElementsByTagName("instructor");
		
							int employee;
							if(nListInstructors.getLength() != 0 ) {
			            	    employee = Integer.parseInt(eNextElement.getElementsByTagName("emplid").item(0).getTextContent());
			            	   
							}
							else {
			            	    employee = 0;
							}
			               
			               int courseID = Integer.parseInt(eNextElement.getElementsByTagName("course_id").item(0).getTextContent());
			               //create new course node and add to symTable
			            
			               
			               CourseNode addCourse = 
			            		   new CourseNode(courseID, 
			            		   eNextElement.getElementsByTagName("title").item(0).getTextContent(), 
			            		   eNextElement.getElementsByTagName("description").item(0).getTextContent(), 
			            		   code,
			            		   eNextElement.getElementsByTagName("catalog_number").item(0).getTextContent(),
			            		   employee,
					               eNextElement.getElementsByTagName("type_name").item(0).getTextContent());
			               
				               symTable.put(courseID, addCourse);
	
				         	}
						}
		        	}
		        }
		
	    //build wordTable
		wordTable = buildWordTable();
	
		}
		
		//if there's a catch, it'll be caught here! 
		catch (Exception e) {
	        e.printStackTrace();
	     }
	}
	
	//get course title from courseID
	public String getTitle(int courseID) {
		CourseNode node = symTable.get(courseID);
		return node.title;
	}
	
	//get course description  from courseID
	public String getDescription(int courseID) {
		CourseNode node = symTable.get(courseID);
		return node.description;
	}
	
	//get course department code from courseID
	public String getDeptCode(int courseID) {
		CourseNode node = symTable.get(courseID);
		return node.departmentCode;
	}

	//get course catalog number from courseID
	public String getCatalogNum(int courseID) {
		CourseNode node = symTable.get(courseID);
		return node.catalogNumber;
	}

	//get course employeeID from courseID
	public int getEmplid(int courseID) {
		CourseNode node = symTable.get(courseID);
		return node.emplid;
	}

	//get course typename from courseID
	public String getTypename(int courseID) {
		CourseNode node = symTable.get(courseID);
		return node.typename;
	}

	//get number of terms in description + title from courseID
	public int getNumTermsInDoc(int courseID) {
		CourseNode node = symTable.get(courseID);
		return node.numTermsInDoc;
	}
	//if we decide to not want numbers, figure dish ish out
	/*private boolean isNumber(char c) {
		int ch = (int)c;
		if (ch >= 48 && ch <= 57) return true;
		return false;
	}
	
	public String[] combineArrays(String[] array1, String[] array2) {
		
		int num = 0;
		for(int i = 0; i < array1.length; i++) {
			if((!array1[i].equals(""))) {
			if (isNumber((array1[i].charAt(0)))) {
				num++;
			}	
		}
		}
		for(int i = 0; i < array2.length; i++) {
			if((!array2[i].equals(""))) {
			if (isNumber((array2[i].charAt(0)))) {
				num++;
			}	
		}
		}
	
		String[] newArray = new String[array1.length + array2.length - num];
		
		int i;
		int index = 0;
		
		for(i = 0; i < array1.length; i++) {
			if  (!array1[i].equals("")) {
			if (!isNumber((array1[i].charAt(0)))){
				newArray[index] = array1[i];
				index++;
			}
		}
		}
		
		int index2 = 0;
		for(int j = 0; j < array2.length; j++) {
			if((!array2[j].equals(""))) {
			if (!isNumber((array2[j].charAt(0)))) {
				newArray[index + index2] = array2[j];
				index2++;
			}
			}
		}
		return newArray;
	}*/
	
	//conglomerate two arrays, return new array
	
	public boolean isStopWord(String s) {
		if (s.equals("a") || s.equals("an") || s.equals("and") ||
				s.equals("are") || s.equals("as") || s.equals("at") ||
				s.equals("be") || s.equals("by") || s.equals("for") ||
				s.equals("from") || s.equals("has") || s.equals("he") ||
				s.equals("in") || s.equals("is") || s.equals("it") ||
				s.equals("its") || s.equals("of") || s.equals("on") ||
				s.equals("that") || s.equals("the") || s.equals("to") ||
				s.equals("was") || s.equals("were") || s.equals("will") ||
				s.equals("with") || s.equals("what") || s.equals("") || s.equals("this")
				|| s.equals("which")) {
			return true;
		}
		return false;
	}

	public String[] combineArrays(String[] array1, String[] array2) {
		int count = 0; 
		
		for (int k = 0; k < array1.length; k++) {
			if (isStopWord(array1[k])) {
				count++;
			}
		}
		
		for (int l = 0; l < array2.length; l++) {
			if (isStopWord(array2[l])) {
				count++;
			}
		}
				
		String[] newArray = new String[array1.length + array2.length - count];
		int index = 0;
		int i;	
		
		for (i = 0; i < array1.length; i++) {
			if (!isStopWord(array1[i])) {
				newArray[index] = array1[i];
				index++;
				}
			}
		
		int index2 = 0;
		for (int j = 0; j < array2.length; j++) {
			if (!isStopWord(array2[j])) {
				newArray[index + index2] = array2[j];
				index2++;
			}
		}
		/*for ( int q = 0; q < newArray.length; q++) {
			System.out.println("Word at " + i + ": " + newArray[q]);
		}*/
		return newArray;
	}
	

	
	//  the word table will contain all word encountered associated with 
	//  the number of courses it appears 
	public ST<String, Integer> buildWordTable() {
		ST<String, Integer> table = new ST<String, Integer>();
		String[] descripWords;
		String[] titleWords;
		String[] combinedArrays;
		int index = 0;
		
		/*for every courseID in the symtable find the description and title, and tokenize into 
		one array*/
		
		for (Integer i: symTable) {
			CourseNode node = symTable.get(i);
			//tokenize, make space on anything that is not a inword character 
			descripWords = node.description.split("\\W");
			titleWords = node.title.split("\\W");
			
			//make lowercase 
			for (int k = 0; k < descripWords.length; k++) {
				descripWords[k] = descripWords[k].toLowerCase();
			}
			
			for (int v = 0; v < titleWords.length; v++) {
				titleWords[v] = titleWords[v].toLowerCase();
			}
			
			//combine array
			combinedArrays = combineArrays(descripWords, titleWords);
			
			//put courseID and string array into toBeVector
			toBeVector.put(i, combinedArrays);
			node.numTermsInDoc = combinedArrays.length;
			ST<String, Boolean> found = new ST<String, Boolean>();
			for (int j = 0; j < combinedArrays.length; j++) {
				if(!found.contains(combinedArrays[j])) {
					if (!table.contains(combinedArrays[j])) {
						table.put(combinedArrays[j], 1);
						found.put(combinedArrays[j], true);
						indexToWord.put(index, combinedArrays[j]);
						wordIndex.put(combinedArrays[j], index++);
					}
					else {
						found.put(combinedArrays[j], true); 
						table.put(combinedArrays[j], table.get(combinedArrays[j]) + 1);
					}		
				}
			}
		}
		
		return table;
	}
	
	//get number of total terms found in all descriptions+titles 
	public int getNumTerms() {
		return wordTable.size();
	}
	
	//get total number of courses
	public int getNumCourses() {
		return symTable.size();
	}
	
	//create a data parser
	public static void main(String[] args) {
		
		DataParser dp = new DataParser();
		System.out.println("From data parser: " + dp.getNumTerms());
		System.out.println("From data parser: " + dp.getNumCourses());

			        
	}
}