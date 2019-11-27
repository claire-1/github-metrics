package com.app.mycompany;

/*
 * FilteredClassifierTrainer.java: Train a basic weka classifier in Java.
 * Based on https://github.com/jmgomezh/tmweka/tree/master/FilteredClassifier
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
     * Object that stores the filter
     */
    StringToWordVector filter;
    /**
     * Object that stores the classifier
     */
    FilteredClassifier classifier;

    public FilteredClassifierTrainer() {
        this.filter = new StringToWordVector();
        this.filter.setAttributeIndices("last");
        this.classifier = new FilteredClassifier();
        this.classifier.setFilter(this.filter);
        this.classifier.setClassifier(new NaiveBayes());
    }

    /**
     * This method loads a dataset in ARFF format. If the file does not exist, or it
     * has a wrong format, the attribute trainData is null.
     * 
     * @param fileName The name of the file that stores the dataset.
     */
    public Instances loadDataset(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            ArffReader arff = new ArffReader(reader);
            Instances trainData = arff.getData();
            reader.close();
            trainData.setClassIndex(0);
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
    public void evaluate(Instances trainingData) {
        try {
            Evaluation eval = new Evaluation(trainingData);
            eval.crossValidateModel(classifier, trainingData, 4, new Random(1));
            // System.out.println(eval.toSummaryString());
            // System.out.println(eval.toClassDetailsString());
        } catch (Exception e) {
            throw new RuntimeException("evaluate|error when evaluating classifier", e);
        }
    }

    /**
     * This method trains the classifier on the loaded dataset.
     */
    public void trainClassifier(Instances trainingData) {
        try {
            trainingData.setClassIndex(0);
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayes());
            classifier.buildClassifier(trainingData);
            // Uncomment to see the classifier
            // System.out.println(classifier);
        } catch (Exception e) {
            throw new RuntimeException("trainClassifier|error when training classifier", e);
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
        } catch (IOException e) {
            throw new RuntimeException("saveModel|error when writing to " + fileName, e);
        }
    }
}