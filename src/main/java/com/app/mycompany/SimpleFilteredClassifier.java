package com.app.mycompany;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * SimpleFilteredClassifier.java: allows for classification of strings using a weka model
 * Based on https://github.com/jmgomezh/tmweka/tree/master/FilteredClassifier
 */
public class SimpleFilteredClassifier {

	/**
	 * Object that stores the instance.
	 */
	// Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	private FilteredClassifier classifier;

	public SimpleFilteredClassifier(String fileName) {
		this.classifier = loadClassifierModel(fileName);
	}
	/**
	 * This method loads the model to be used as classifier.
	 * 
	 * @param fileName The name of the file that stores the text.
	 */
	private FilteredClassifier loadClassifierModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
			Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
			in.close();
			System.out.println("===== Loaded model: " + fileName + " =====");
			return classifier;
		} catch (Exception e) {
			// Given the cast, a ClassNotFoundException must be caught along with the
			// IOException
			System.out.println("Problem found when reading: " + fileName);
			throw new RuntimeException("loadModel|error when reading " + fileName, e);
		}
	}

	/**
	 * This method creates the instance to be classified, from the text that has
	 * been read.
	 */
	public Instances makeInstanceInInstances(String data, String attributeName1, String attributeName2) {
		// Create the attributes, label and text
		ArrayList<String> fvNominalVal = new ArrayList<String>();
		fvNominalVal.add(attributeName1);
		fvNominalVal.add(attributeName2);

		Attribute attribute1 = new Attribute("label", fvNominalVal);
		Attribute attribute2 = new Attribute("text", (List<String>) null);

		// Add the attributes to a list
		ArrayList<Attribute> fvWekaAttributes = new ArrayList<>();
		fvWekaAttributes.add(attribute1);
		fvWekaAttributes.add(attribute2);

		Instances instances = new Instances("Test relation", fvWekaAttributes, 1);
		// Set class index
		instances.setClassIndex(0);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute2, data);
		instances.add(instance);
		return instances;
	}

	/**
	 * This method performs the classification of the instance. Output is done at
	 * the command-line.
	 */
	public String classify(Instances unlabledDataInstance) {
		try {
			double pred = classifier.classifyInstance(unlabledDataInstance.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: " + unlabledDataInstance.classAttribute().value((int) pred));
			return unlabledDataInstance.classAttribute().value((int) pred);
		} catch (Exception e) {
			System.out.println("Problem found when classifying the text");
			throw new RuntimeException("classify|error when classifying the text", e);
		}
	}
}