package com.app.mycompany;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class implements a simple text classifier in Java using WEKA.
 * It loads a file with the text to classify, and the model that has been
 * learnt with MyFilteredLearner.java.
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see MyFilteredLearner
 */
 public class SimpleFilteredClassifier {

	/**
	 * Object that stores the instance.
	 */
	Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	FilteredClassifier classifier;
			
	/**
	 * This method loads the model to be used as classifier.
	 * @param fileName The name of the file that stores the text.
	 */
	public void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
            in.close();
 			System.out.println("===== Loaded model: " + fileName + " =====");
       } 
		catch (Exception e) {
			// Given the cast, a ClassNotFoundException must be caught along with the IOException
			System.out.println("Problem found when reading: " + fileName);
			throw new RuntimeException("loadModel|error when reading " + fileName, e);
		}
	}
	
	/**
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public Instance makeInstance(String data, String attributeName1, String attributeName2) {
		// Create the attributes, class and text
        ArrayList<String> fvNominalVal = new ArrayList<String>();
		fvNominalVal.add(attributeName1);
		fvNominalVal.add(attributeName2);
		Attribute attribute1 = new Attribute("label", fvNominalVal);
        Attribute attribute2 = new Attribute("text", (List < String >) null);
   
           // Declare the feature vector
          ArrayList<Attribute> fvWekaAttributes = new ArrayList < > ();
          fvWekaAttributes.add(attribute1);
          fvWekaAttributes.add(attribute2);


		instances = new Instances("Test relation", fvWekaAttributes, 1);           
		// Set class index
		instances.setClassIndex(0);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute2, data);
		// // Another way to do it:
		// // instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		instances.add(instance); // TODO delete later
 		// System.out.println("===== Instance created with reference dataset =====");
		// System.out.println(instances);
		return instance;
	}
	
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 */
	public String classify() {
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("===== Classified instance =====");
            System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
            return instances.classAttribute().value((int) pred);
		}
		catch (Exception e) {
            System.out.println("Problem found when classifying the text");
            throw new RuntimeException("classify|error when classifying the text", e);
		}		
	}
}	