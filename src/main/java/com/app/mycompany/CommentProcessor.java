package com.app.mycompany;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kohsuke.github.GHIssueComment;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class CommentProcessor {

    private Connection conn;
    // private String connectionUrl;
    // private String userName;

    public CommentProcessor(String hostnameAndPort, String databaseName, String userName) {
        // TODO source:
        // https://javarevisited.blogspot.com/2016/09/javasqlsqlexception-no-suitable-driver-mysql-jdbc-localhost.html#ixzz64gKoyLbl

        try {
            String connectionUrl = "jdbc:mysql://" + hostnameAndPort + "/" + databaseName;
            Properties info = new Properties();
            info.put("user", userName);
            info.put("password", "root"); // TODO make this a parameter if works with PHP

            conn = DriverManager.getConnection(connectionUrl, info);
        } catch (SQLException e) {
            throw new RuntimeException("CommentProcessor|cannot connect to mysql database", e);
        }
    }

    public void putCommentInDB(long issueId, Date issueClosedDate, String comment) throws SQLException {
        String query = " insert into comments (relatedIssueId, dateIssueClosed, content)" + " values (?, ?, ?)";

        // Source TODO
        // https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setLong(1, issueId);
        preparedStmt.setDate(2, issueClosedDate);
        preparedStmt.setString(3, comment);
        // execute the query
        preparedStmt.execute();
    }

    public void putCommentsInDB(long issueId, Date issueClosedDate, List<GHIssueComment> comments) throws SQLException {
        for (GHIssueComment comment : comments) {
            // TODO how to get the timestamp from the comments?
            putCommentInDB(issueId, issueClosedDate, comment.getBody());
        }
    }

    // TODO delete this probably
    public String getDataFromDatabase(String query) throws SQLException { // TODO this is just for testing for now
        // Source for dealing with ResultSets:
        // https://www.javatpoint.com/example-to-connect-to-the-mysql-database TODO
        System.out.println("getting data");
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        String dataString = "";
        while (rs.next()) {
            System.out.println("COMMENTS " + rs.getString(1));
            // for (int i = 0; i < rs.getFetchSize(); i++) {
            // dataString += rs.getString(i);
            // }
            dataString += rs.getString(1);
            // comments += rs.getString(2);
        }

        return dataString;
    }

    // TODO source
    // http://biercoff.com/nice-and-simple-converter-of-java-resultset-into-jsonarray-or-xml/
    public static JSONArray convertToJSON(ResultSet resultSet) throws JSONException, SQLException {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), 
                    resultSet.getObject(i + 1));   
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public ResultSet getDataFromDatabaseAsResultSet(String query) throws SQLException { // TODO this is just for testing for now
        // Source for dealing with ResultSets:
        // https://www.javatpoint.com/example-to-connect-to-the-mysql-database TODO
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        return rs;
    }

    public Instances getAsDataSetFromSql(String query2) {
        // TODO source: https://waikato.github.io/weka-wiki/use_weka_in_your_java_code/
        try {
            InstanceQuery query = new InstanceQuery();
            query.setUsername("root");
            query.setPassword("root");
            query.setQuery("select content from comments");
            // query.setSparseData(true); // doesn't store info that is zero. TODO want
            // this?
            // https://waikato.github.io/weka-wiki/faqs/why_am_i_missing_certain_nominal_or_string_values_from_sparse_instances/
            Instances data = query.retrieveInstances();
            data.setClassIndex(data.numAttributes() - 1);
            System.out.println("DATA HERE " + data.toString());
            return data;
        } catch (Exception e) {
            throw new RuntimeException("getAsDataSet|problem connecting with database loader", e);
        }
    }

    // To use for training data in arff file
    public Instances getDataSetFromFile(String filename) {
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

    public String classifyData(Instances trainingData, Instances dataToBeClassified) throws Exception {
        // TODO source: https://www.codingame.com/playgrounds/6734/machine-learning-with-java---part-5-naive-bayes
        Classifier classifier = new NaiveBayesMultinomial();
        classifier.buildClassifier(trainingData);
        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier, dataToBeClassified);
        System.out.println("** Naive Bayes Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.println(classifier);
        // eval.evaluateModel(classifier, dataToBeClassified);
        int classifiedIssue = 0; // The first result from the instances is the classification for the whole issue
                                 // the remaining issues are the classifications for each comment on the issue
       // System.out.println(dataToBeClassified.instance(classifiedIssue));
        double index = classifier.classifyInstance(dataToBeClassified.instance(classifiedIssue));
        
        String classification = trainingData.attribute(0).value((int) index);
        System.out.println(classification);
        System.out.println("HELLO");
        return classification;
    }

    public void manipulateData(String query) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.execute();
    }
    public void putClassificationInDB(long issueId, Date issueClosedDate, String classification) throws SQLException {
        String query = " insert into classifierResults (relatedIssueId, dateIssueClosed, classifiedIssueStatus)"
                + " values (?, ?, ?)";

        // Source TODO
        // https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setLong(1, issueId);
        preparedStmt.setDate(2, issueClosedDate);
        preparedStmt.setString(3, classification);
        // execute the query
        preparedStmt.execute();
    }
}