package com.app.mycompany;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class CommentProcessor {

    private ArrayList<Attribute> wekaAttributes;
   // private FilteredClassifier classifier;
   private NaiveBayesMultinomial classifier;

    public CommentProcessor(Instances trainingData, String attributeLabel1, String attributeLabel2) throws Exception {
        this.classifier = new NaiveBayesMultinomial();
        classifier.buildClassifier(trainingData);
        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier, trainingData);
        System.out.println("** Naive Bayes Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.println(classifier);
        //classifier = new FilteredClassifier();

        // set Multinomial NaiveBayes as arbitrary classifier
       // classifier.setClassifier(new NaiveBayesMultinomial());

        // TODO need filter?
        // create the filter and set the attribute to be transformed from text into a
        // feature vector (the last one)
        // StringToWordVector filter = new StringToWordVector();
        // filter.setAttributeIndices("last");

        // // add ngram tokenizer to filter with min and max length set to 1
        // NGramTokenizer tokenizer = new NGramTokenizer();
        // tokenizer.setNGramMinSize(1);
        // tokenizer.setNGramMaxSize(1);
        // // use word delimeter
        // tokenizer.setDelimiters("\\W");
        // filter.setTokenizer(tokenizer);

        // // convert tokens to lowercase
        // filter.setLowerCaseTokens(true);

        // // add filter to classifier
        // classifier.setFilter(filter);

        // Declare text attribute to hold the message for the Instance
        Attribute attributeText = new Attribute("text", (List<String>) null);

        // Declare the label attribute along with its values
        ArrayList<String> classAttributeValues = new ArrayList<>();
        classAttributeValues.add(attributeLabel1);
        classAttributeValues.add(attributeLabel2);
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

     // To use for training data in arff file
     public static Instances getDataSetFromFile(String filename) {
        // TODO Taken from source:
        // https://www.codingame.com/playgrounds/6734/machine-learning-with-java---part-5-naive-bayes

        StringToWordVector filter = new StringToWordVector();
        ArffLoader loader = new ArffLoader();
        try {
            File fileOfData = new File(filename);
            InputStream dataStream = new FileInputStream(fileOfData);
            loader.setSource(dataStream);

            Instances dataSet = loader.getDataSet();
            // TODO explaination of class index source:
            // https://stackoverflow.com/questions/26734189/what-is-class-index-in-weka
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
            filter.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, filter);
            return dataSet;
        } catch (IOException e) {
            throw new RuntimeException("getDataSetFromFile|can't access file", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("getDataSetFromFile|can't set class index", e);
        } catch (Exception e) {
            throw new RuntimeException("getDataSetFromFile|can't set input format", e);
        } // TODO maybe should just throw these to a higher level; fine for now
    }

    public String classifyData(Instances trainingData, String dataToBeClassified) throws Exception {
        // create new Instance for prediction.
        int numAttributesFromArff = 2; // for label and text
        StringToWordVector filter = new StringToWordVector();
        // weka demand a dataset to be set to new Instance
        Instances newDataset = new Instances("predictiondata", wekaAttributes, 1);
        newDataset.setClassIndex(0); // makes data look like: resolved, 'some text'
        filter.setInputFormat(newDataset);
        newDataset = Filter.useFilter(newDataset, filter);

        DenseInstance newInstance = new DenseInstance(numAttributesFromArff);
        newInstance.setDataset(newDataset);

        // text attribute value set to value to be predicted
        newInstance.setValue(wekaAttributes.get(1), dataToBeClassified);

        // predict most likely class for the instance
        double pred = classifier.classifyInstance(newInstance);

        return newDataset.classAttribute().value((int) pred);
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
}