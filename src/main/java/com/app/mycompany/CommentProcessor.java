package com.app.mycompany;

import java.util.ArrayList;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;

public class CommentProcessor {

    private ArrayList<Attribute> wekaAttributes;
    // private FilteredClassifier classifier;
    private FilteredClassifier classifier;

    // public CommentProcessor(Instances trainingData, String attributeLabel1, String attributeLabel2) throws Exception {

    //     classifier = new FilteredClassifier();

    //     StringToWordVector filter = new StringToWordVector();
    //     trainingData.setClassIndex(0);
    //     // TODO source about filer
    //     // http://jmgomezhidalgo.blogspot.com/2013/06/sample-code-for-text-indexing-with-weka.html
    //     filter.setInputFormat(trainingData);
    //     filter.setLowerCaseTokens(true);
    //     filter.setWordsToKeep(1000000);
    //     filter.setDoNotOperateOnPerClassBasis(true);
    //     filter.setAttributeIndices("last");
    //     // // add ngram tokenizer to filter with min and max length set to 1
    //     NGramTokenizer tokenizer = new NGramTokenizer();
    //     tokenizer.setNGramMinSize(1);
    //     tokenizer.setNGramMaxSize(1);
    //     // use word delimeter
    //     tokenizer.setDelimiters("\\W");
    //     filter.setTokenizer(tokenizer);
    //     // set Multinomial NaiveBayes as arbitrary classifier
    //     classifier.setClassifier(new NaiveBayesMultinomial());

    //     // this.classifier = new NaiveBayesMultinomial();
    //     classifier.buildClassifier(trainingData);
    //     Evaluation eval = new Evaluation(trainingData);
    //     eval.evaluateModel(classifier, trainingData);
    //     System.out.println("** Naive Bayes Evaluation with Datasets **");
    //     System.out.println(eval.toSummaryString());
    //     System.out.println(classifier);
    //     Thread.sleep(5000);

    //     // TODO need filter?
    //     // create the filter and set the attribute to be transformed from text into a
    //     // feature vector (the last one)
    //     // StringToWordVector filter = new StringToWordVector();
    //     // filter.setAttributeIndices("last");

    //     // // add ngram tokenizer to filter with min and max length set to 1
    //     // NGramTokenizer tokenizer = new NGramTokenizer();
    //     // tokenizer.setNGramMinSize(1);
    //     // tokenizer.setNGramMaxSize(1);
    //     // // use word delimeter
    //     // tokenizer.setDelimiters("\\W");
    //     // filter.setTokenizer(tokenizer);

    //     // // convert tokens to lowercase
    //     // filter.setLowerCaseTokens(true);

    //     // // add filter to classifier
    //     // classifier.setFilter(filter);

    //     // Declare text attribute to hold the message for the Instance
    //     Attribute attributeText = new Attribute("text", (List<String>) null);

    //     // Declare the label attribute along with its values
    //     ArrayList<String> classAttributeValues = new ArrayList<>();
    //     classAttributeValues.add(attributeLabel1);
    //     classAttributeValues.add(attributeLabel2);
    //     Attribute classAttribute = new Attribute("label", classAttributeValues);

    //     // Declare the feature vector
    //     this.wekaAttributes = new ArrayList<>();
    //     this.wekaAttributes.add(classAttribute);
    //     this.wekaAttributes.add(attributeText);
    // }

    // To use for training data in arff file
    // public static Instances getDataSetFromFile(String filename) {
    // // TODO Taken from source:
    // //
    // https://www.codingame.com/playgrounds/6734/machine-learning-with-java---part-5-naive-bayes

    // try {

    // ArffLoader loader = new ArffLoader();
    // File fileOfData = new File(filename);
    // InputStream dataStream = new FileInputStream(fileOfData);
    // loader.setSource(dataStream);

    // Instances dataSet = loader.getDataSet();
    // // TODO explaination of class index source:
    // // https://stackoverflow.com/questions/26734189/what-is-class-index-in-weka
    // // dataSet.setClassIndex(dataSet.numAttributes() - 1);
    // dataSet.setClassIndex(0);

    // StringToWordVector filter = new StringToWordVector();
    // // TODO source about filer
    // http://jmgomezhidalgo.blogspot.com/2013/06/sample-code-for-text-indexing-with-weka.html
    // filter.setInputFormat(dataSet);
    // filter.setLowerCaseTokens(true);
    // filter.setWordsToKeep(1000000);
    // filter.setDoNotOperateOnPerClassBasis(true);
    // filter.setAttributeIndices("last");
    // // // add ngram tokenizer to filter with min and max length set to 1
    // NGramTokenizer tokenizer = new NGramTokenizer();
    // tokenizer.setNGramMinSize(1);
    // tokenizer.setNGramMaxSize(1);
    // // use word delimeter
    // tokenizer.setDelimiters("\\W");
    // filter.setTokenizer(tokenizer);

    // dataSet = Filter.useFilter(dataSet, filter);
    // return dataSet;
    // } catch (IOException e) {
    // throw new RuntimeException("getDataSetFromFile|can't access file", e);
    // } catch (IllegalArgumentException e) {
    // throw new RuntimeException("getDataSetFromFile|can't set class index", e);
    // } catch (Exception e) {
    // throw new RuntimeException("getDataSetFromFile|can't set input format", e);
    // } // TODO maybe should just throw these to a higher level; fine for now
    // }

    // public String classifyData(Instances trainingData, String dataToBeClassified)
    // throws Exception {

    // }
}