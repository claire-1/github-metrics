package com.app.mycompany;

/**
 * A Java class that implements a simple text learner, based on WEKA.
 * To be used with MyFilteredClassifier.java.
 * WEKA is available at: http://www.cs.waikato.ac.nz/ml/weka/
 * Copyright (C) 2013 Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 *
 * This program is free software: you can redistribute it and/or modify
 * it for any purpose.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * 
 * TODO source: https://github.com/jmgomezh/tmweka/blob/master/FilteredClassifier/MyFilteredLearner.java
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * This class implements a simple text learner in Java using WEKA. It loads a
 * text dataset written in ARFF format, evaluates a classifier on it, and saves
 * the learnt model for further use.
 * 
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see MyFilteredClassifier
 */
public class FilteredClassifierTrainer {

    /**
     * Object that stores training data.
     */
    Instances trainData;
    /**
     * Object that stores the filter
     */
    StringToWordVector filter;
    /**
     * Object that stores the classifier
     */
    FilteredClassifier classifier;

    /**
     * This method loads a dataset in ARFF format. If the file does not exist, or it
     * has a wrong format, the attribute trainData is null.
     * 
     * @param fileName The name of the file that stores the dataset.
     */
    // public MyFilteredLearner(String trainingData) {

    // }
    public Instances loadDataset(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            ArffReader arff = new ArffReader(reader);
            trainData = arff.getData();
            System.out.println("===== Loaded dataset: " + fileName + " =====");
            reader.close();
            return trainData;
        } catch (IOException e) {
            throw new RuntimeException("loadDataset|error when reading " + fileName, e);
        }
    }

    /**
     * This method evaluates the classifier. As recommended by WEKA documentation,
     * the classifier is defined but not trained yet. Evaluation of previously
     * trained classifiers can lead to unexpected results.
     */
    public void evaluate() {
        try {
            trainData.setClassIndex(0);
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayes());
            Evaluation eval = new Evaluation(trainData);
            eval.crossValidateModel(classifier, trainData, 4, new Random(1));
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toClassDetailsString());
            System.out.println("===== Evaluating on filtered (training) dataset done =====");
        } catch (Exception e) {
            System.out.println("Problem found when evaluating");
        }
    }

    /**
     * This method trains the classifier on the loaded dataset.
     */
    public void learn() {
        try {
            trainData.setClassIndex(0);
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayes());
            classifier.buildClassifier(trainData);
            // Uncomment to see the classifier
            // System.out.println(classifier);
            System.out.println("===== Training on filtered (training) dataset done =====");
        } catch (Exception e) {
            System.out.println("Problem found when training");
        }
    }

    /**
     * This method saves the trained model into a file. This is done by simple
     * serialization of the classifier object.
     * 
     * @param fileName The name of the file that will store the trained model.
     */
    public void saveModel(String fileName) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(classifier);
            out.close();
            System.out.println("===== Saved model: " + fileName + " =====");
        } catch (IOException e) {
            System.out.println("Problem found when writing: " + fileName);
        }
    }
}