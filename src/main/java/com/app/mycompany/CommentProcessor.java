package com.app.mycompany;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class CommentProcessor {

    private Connection conn;
    private ArrayList<Attribute> wekaAttributes;
    // private String connectionUrl;
    // private String userName;

    public CommentProcessor() {
        // Declare text attribute to hold the message for the Instance
        Attribute attributeText = new Attribute("text", (List<String>) null);

        // Declare the label attribute along with its values
        ArrayList<String> classAttributeValues = new ArrayList<>();
        classAttributeValues.add("resolved");
        classAttributeValues.add("unresolved");
        Attribute classAttribute = new Attribute("label", classAttributeValues);

        // Declare the feature vector
        this.wekaAttributes = new ArrayList<>();
        this.wekaAttributes.add(classAttribute);
        this.wekaAttributes.add(attributeText);
    }

    // TODO source
    // http://biercoff.com/nice-and-simple-converter-of-java-resultset-into-jsonarray-or-xml/
    public static JSONArray convertToJSON(ResultSet resultSet) throws JSONException, SQLException {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    // public String classifyData(Instances trainingData, Instances
    // dataToBeClassified) throws Exception {
    // Classifier classifier = new NaiveBayesMultinomial();
    // classifier.buildClassifier(trainingData);
    // Evaluation eval = new Evaluation(trainingData);
    // eval.evaluateModel(classifier, dataToBeClassified);
    // System.out.println("** Naive Bayes Evaluation with Datasets **");
    // System.out.println(eval.toSummaryString());
    // System.out.println(classifier);
    // // eval.evaluateModel(classifier, dataToBeClassified);
    // int classifiedIssue = 0; // The first result from the instances is the
    // classification for the whole issue
    // // the remaining issues are the classifications for each comment on the issue
    // // System.out.println(dataToBeClassified.instance(classifiedIssue));
    // double index =
    // classifier.classifyInstance(dataToBeClassified.instance(classifiedIssue));

    // String classification = trainingData.attribute(0).value((int) index);
    // System.out.println(classification);
    // System.out.println("HELLO");
    // return classification;
    // }

    public String classifyData(Instances trainingData, String dataToBeClassified) throws Exception {
        Classifier classifier = new NaiveBayesMultinomial();
        classifier.buildClassifier(trainingData);
        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier, trainingData);
        System.out.println("** Naive Bayes Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.println(classifier);

        // create new Instance for prediction.
        DenseInstance newInstance = new DenseInstance(2);

        // weka demand a dataset to be set to new Instance
        Instances newDataset = new Instances("predictiondata", wekaAttributes, 1);
        newDataset.setClassIndex(0);

        newInstance.setDataset(newDataset);

        // text attribute value set to value to be predicted
        newInstance.setValue(wekaAttributes.get(1), dataToBeClassified);

        // predict most likely class for the instance
        double pred = classifier.classifyInstance(newInstance);

        return newDataset.classAttribute().value((int) pred);
    }
}