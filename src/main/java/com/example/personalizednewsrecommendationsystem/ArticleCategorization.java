package com.example.personalizednewsrecommendationsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.doccat.*;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class ArticleCategorization {
    public static void main(String[] args) throws IOException {
        String datasetPath = "src/main/resources/com/example/personalizednewsrecommendationsystem/News Article Dataset without category.csv";
        String outputPath = "src/main/resources/com/example/personalizednewsrecommendationsystem/News Article Dataset.csv";

        // Load dataset
        List<String[]> articles = loadCSV(datasetPath);

        // Step 2: Train the classifier
        DoccatModel model = trainClassifier();

        // Step 3: Predict categories and update dataset
        List<String[]> updatedArticles = new ArrayList<>();
        for (String[] article : articles) {
            String title = article[0];
            String content = article[1];
            String category = predictCategory(model, title);
            updatedArticles.add(new String[]{category, title, content});
        }

        // Step 4: Save the updated dataset
        saveCSV(outputPath, updatedArticles);
    }

    private static List<String[]> loadCSV(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<String[]> data = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            data.add(values);
        }
        reader.close();
        return data;
    }

    private static void saveCSV(String path, List<String[]> data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String[] record : data) {
            writer.write(String.join(",", record));
            writer.newLine();
        }
        writer.close();
    }



    private static DoccatModel trainClassifier() throws IOException {
        InputStream dataIn = new FileInputStream("src/main/resources/com/example/personalizednewsrecommendationsystem/News Article Training Dataset.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(dataIn));

        List<String> processedLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            int firstCommaIndex = line.indexOf(",");
            if (firstCommaIndex != -1) {
                String processedLine = line.substring(0, firstCommaIndex) + "\t" + line.substring(firstCommaIndex + 1);
                processedLines.add(processedLine);
            }
        }
        reader.close();

        ObjectStream<String> lineStream = new PlainTextByLineStream(() -> new ByteArrayInputStream(String.join("\n", processedLines).getBytes()), "UTF-8");
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, "100");
        params.put(TrainingParameters.CUTOFF_PARAM, "1");

        DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

        // Save the model
        try (OutputStream modelOut = new FileOutputStream("src/main/resources/com/example/personalizednewsrecommendationsystem/news-category-model.bin")) {
            model.serialize(modelOut);
        }

        return model;
    }




    static String predictCategory(DoccatModel model, String title) {
        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
        double[] outcomes = categorizer.categorize(title);
        return categorizer.getBestCategory(outcomes);
    }


    public static DoccatModel loadTrainedModel() throws IOException {
        try (InputStream modelIn = new FileInputStream("src/main/resources/com/example/personalizednewsrecommendationsystem/news-category-model.bin")) {
            return new DoccatModel(modelIn);
        }
    }



}



