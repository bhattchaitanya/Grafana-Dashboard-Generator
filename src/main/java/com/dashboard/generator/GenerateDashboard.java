package com.dashboard.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GenerateDashboard {

    public static void main(final String[] args) {

        final List<String> metrics = new ArrayList<>();

        System.out.println("1.Enter a name for the Grafana dashboard:");
        final Scanner outputFileNameScanner = new Scanner(System.in);
        String outputFileName = "";
        boolean isValidFile = false;
        while ((outputFileName.isEmpty()) || (isValidFile == false)) {
            outputFileName = outputFileNameScanner.nextLine();
            if (outputFileName.isEmpty()) {
                System.out.println("Dashboard name cannot be empty!\n");
                continue;
            } else {
                isValidFile = outputFileName.matches("[a-zA-Z0-9]*");
            }
            if (!isValidFile) {
                System.out.println("Illegal file name. Special Characters cannot be used!\n");
                continue;
            }

        }

        System.out.println("2.Enter output file path (Example: /var/tmp/ on Linux OR like C:\\tmp\\output\\ on Windows):");
        final Scanner outputFilePathScanner = new Scanner(System.in);
        String outputFilePath = "";
        boolean isValidPath = false;
        while ((outputFilePath.isEmpty()) || (isValidPath == false)) {
            outputFilePath = outputFilePathScanner.nextLine();
            if (outputFilePath.isEmpty()) {
                System.out.println("Output path cannot be empty!");
                continue;
            }
            if (!outputFilePath.isEmpty()) {
                if (!(new File(outputFilePath).isDirectory())) {
                    System.out.println("Invalid directory! Enter a valid output path.");
                } else {
                    isValidPath = true;
                } //end of inner if
            } //end of outer if
        } //end of while
        if (!outputFilePath.endsWith("/")) {
            outputFilePath += "/";
        }

        System.out.println("3.Enter path of script(Example: /var/tmp/test.jmx in Linux OR like C:\\tmp\\test.jmx in Windows):");
        final Scanner scriptFileScanner = new Scanner(System.in);
        String scriptPath = "";
        boolean isValidScriptPath = false;
        while ((outputFilePath.isEmpty()) || (isValidScriptPath == false)) {
            scriptPath = outputFilePathScanner.nextLine();
            if (scriptPath.isEmpty()) {
                System.out.println("Script path cannot be empty!");
                continue;
            }
            if (!scriptPath.isEmpty()) {
                if (!(new File(scriptPath).isFile())) {
                    System.out.println("Invalid script file! Enter a valid script path.");
                    continue;
                } else {
                    isValidScriptPath = true;
                } //end of inner if
            } //end of outer if
            if (!scriptPath.endsWith(".jmx")) {
                System.out.println("Invalid script file! Enter a valid script path. Note:Path should contain the file!");
                isValidScriptPath = false;
                continue;
            } //end of outer if
        } //end of while

        System.out.println("Do you want to use the default rootPrefix value? (\"jmeter.\"), Y/N?");
        final Scanner rootPrefixScanner = new Scanner(System.in);
        ;

        String rootPrefix = "";
        boolean isValidOption = false;
        boolean isValidRootPrefix = false;
        String rootPrefixOption = "";
        while (!isValidOption) {
            while ((rootPrefix.isEmpty()) || !isValidRootPrefix) {
                rootPrefixOption = rootPrefixScanner.nextLine();
                if (rootPrefixOption.toLowerCase().equals("yes") || rootPrefixOption.toLowerCase().equals("y")) {
                    rootPrefix = "jmeter.";
                    isValidOption = true;
                    isValidRootPrefix = true;

                } else if (rootPrefixOption.toLowerCase().equals("no") || rootPrefixOption.toLowerCase().equals("n")) {
                    System.out.println("Enter desired rootPrefix value(Note:Special characters are NOT allowed.)");
                    while (!isValidRootPrefix) {
                        rootPrefix = rootPrefixScanner.nextLine();
                        if (!rootPrefix.matches("[a-zA-Z0-9]*")) {
                            System.out.println("Invalid rootPrefix! Note: Special characters are NOT allowed!");
                            rootPrefix = "";
                            continue;
                        } else {
                            rootPrefix = rootPrefix.toLowerCase();
                            rootPrefix += ".";
                            isValidRootPrefix = true;
                        }
                        isValidOption = true;
                    }
                } else {
                    System.out.println("Invalid option! To chose option type: yes/no or y/n.");
                    continue;
                }
            }
        }

        System.out.println("You selected the following rootPrefix : " + rootPrefix);

        System.out.println("Selected Script = " + scriptPath);
        Scanner reader = null;
        if (scriptPath != null) {
            try {
                reader = new Scanner(new File(scriptPath));
            } catch (final FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        String tempLineReadString = null;
        System.out.println("Parsing the Jmeter script and looking for all HTTP transactions...");
        System.out.println("The dashboard will contain the following transactions");

        int lineReaderCount = 0;
        while (reader.hasNextLine()) {
            tempLineReadString = reader.nextLine();
            if (tempLineReadString.contains("<HTTPSamplerProxy")) {
                if (tempLineReadString.contains("enabled=\"true\"")) {

                    final int startIndex = tempLineReadString.indexOf("testname=\"");
                    final int endIndex = tempLineReadString.indexOf("\" enabled");
                    final String testPlanName = tempLineReadString.substring(
                            startIndex + 10, endIndex);
                    metrics.add(testPlanName);
                    tempLineReadString = null;
                    System.out.println(++lineReaderCount + "." + testPlanName);
                }
            }
        }
        String columnName = "";
        String rowName = "";

        int counter = 1;
        int counter2 = 2;
        int counter3 = 3;

        StringBuilder jason = null;
        jason = new StringBuilder();

        final String[] aggregationMetric = {"all.ok.count", "all.ko.count",
                "all.a.pct90", "test.meanAT"};
        int iCounter = 0;
        int idCounter = 88;

        final String head = "{\n" +
                "  \"id\": null,\n" +
                "  \"title\": \"" + outputFileName + "\",\n" +
                "  \"originalTitle\": \"" + outputFileName + "\",\n" +
                "  \"tags\": [],\n" +
                "  \"style\": \"light\",\n" +
                "  \"timezone\": \"browser\",\n" +
                "  \"editable\": true,\n" +
                "  \"hideControls\": false,\n" +
                "  \"sharedCrosshair\": false,\n" +
                "  \"rows\": [\n" +
                "    {\n" +
                "      \"title\": \"Cumulated Metrics\",\n" +
                "      \"height\": \"250px\",\n" +
                "      \"editable\": true,\n" +
                "      \"collapse\": false,\n" +
                "      \"panels\": [\n";

        jason.append(head);
        while (iCounter < aggregationMetric.length) {
            final String object = "{\n" +
                    "          \"title\": \"" + rootPrefix + aggregationMetric[iCounter] + "\",\n" +
                    "          \"error\": false,\n" +
                    "          \"span\": 6,\n" +
                    "          \"editable\": true,\n" +
                    "          \"type\": \"graph\",\n" +
                    "          \"id\": " + idCounter + ",\n" +
                    "          \"datasource\": null,\n" +
                    "          \"renderer\": \"flot\",\n" +
                    "          \"x-axis\": true,\n" +
                    "          \"y-axis\": true,\n" +
                    "          \"y_formats\": [\n" +
                    "            \"short\",\n" +
                    "            \"short\"\n" +
                    "          ],\n" +
                    "          \"grid\": {\n" +
                    "            \"leftMax\": null,\n" +
                    "            \"rightMax\": null,\n" +
                    "            \"leftMin\": null,\n" +
                    "            \"rightMin\": null,\n" +
                    "            \"threshold1\": null,\n" +
                    "            \"threshold2\": null,\n" +
                    "            \"threshold1Color\": \"rgba(216, 200, 27, 0.27)\",\n" +
                    "            \"threshold2Color\": \"rgba(234, 112, 112, 0.22)\"\n" +
                    "          },\n" +
                    "          \"lines\": true,\n" +
                    "          \"fill\": 0,\n" +
                    "          \"linewidth\": 1,\n" +
                    "          \"points\": false,\n" +
                    "          \"pointradius\": 5,\n" +
                    "          \"bars\": false,\n" +
                    "          \"stack\": false,\n" +
                    "          \"percentage\": false,\n" +
                    "          \"legend\": {\n" +
                    "            \"show\": true,\n" +
                    "            \"values\": false,\n" +
                    "            \"min\": false,\n" +
                    "            \"max\": false,\n" +
                    "            \"current\": false,\n" +
                    "            \"total\": false,\n" +
                    "            \"avg\": false\n" +
                    "          },\n" +
                    "          \"nullPointMode\": \"connected\",\n" +
                    "          \"steppedLine\": false,\n" +
                    "          \"tooltip\": {\n" +
                    "            \"value_type\": \"cumulative\",\n" +
                    "            \"shared\": false\n" +
                    "          },\n" +
                    "          \"targets\": [\n" +
                    "            {\n" +
                    "              \"function\": \"mean\",\n" +
                    "              \"column\": \"value\",\n" +
                    "              \"rawQuery\": true,\n" +
                    "              \"query\": \"select * from \\\"" + rootPrefix + aggregationMetric[iCounter] + "\\\" where $timeFilter\"\n" +
                    "            }\n" +
                    "          ],\n" +
                    "          \"aliasColors\": {},\n" +
                    "          \"seriesOverrides\": [],\n" +
                    "          \"links\": []\n" +
                    "        }";

            jason.append(object);

            if ((iCounter + 1) < aggregationMetric.length) {
                jason.append(",\n");
            }
            iCounter++;
            idCounter++;

        }
        jason.append("],\n" +
                "      \"showTitle\": true\n" +
                "    },\n");

        final String[] individualMetrics = {"a.pct90", "ok.count", "ko.count"};
        // display to console
        for (int i = 0; i < metrics.size(); i++) {
            columnName = metrics.get(i);

            rowName = "\"" + columnName + "\"";
            jason.append("{ \"title\": ");
            jason.append(rowName);
            jason.append(", \"height\": \"250px\", \"editable\": true, \"collapse\": false, \"panels\": [ { \"title\": \"" + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[0] + "\", \"error\": false, \"span\": 4, \"editable\": true, \"type\": \"graph\", \"id\": ");
            jason.append(counter);
            jason.append(
                    ", \"datasource\": null, \"renderer\": \"flot\", \"x-axis\": true, \"y-axis\": true, \"y_formats\": [ \"short\", \"short\" ], \"grid\": { \"leftMax\": null, \"rightMax\": null, \"leftMin\": null, \"rightMin\": null, \"threshold1\": null, \"threshold2\": null, \"threshold1Color\": \"rgba(216, 200, 27, 0.27)\", \"threshold2Color\": \"rgba(234, 112, 112, 0.22)\" }, \"lines\": true, \"fill\": 0, \"linewidth\": 1, \"points\": false, \"pointradius\": 5, \"bars\": false, \"stack\": false, \"percentage\": false, \"legend\": { \"show\": true, \"values\": false, \"min\": false, \"max\": false, \"current\": false, \"total\": false, \"avg\": false }, \"nullPointMode\": \"connected\", \"steppedLine\": false, \"tooltip\": { \"value_type\": \"cumulative\", \"shared\": false }, \"targets\": [ { \"function\": \"mean\", \"column\": \"value\", \"series\": \""
                            + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[0] + "\", \"query\": \"select * from \\\"" + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[0] + "\\\" where $timeFilter\", \"rawQuery\": true } ], \"aliasColors\": {}, \"seriesOverrides\": [] }, { \"title\": \"" + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[1] + "\", \"error\": false, \"span\": 4, \"editable\": true, \"type\": \"graph\", \"id\": ");
            jason.append(counter2);
            jason.append(
                    ", \"datasource\": null, \"renderer\": \"flot\", \"x-axis\": true, \"y-axis\": true, \"y_formats\": [ \"short\", \"short\" ], \"grid\": { \"leftMax\": null, \"rightMax\": null, \"leftMin\": null, \"rightMin\": null, \"threshold1\": null, \"threshold2\": null, \"threshold1Color\": \"rgba(216, 200, 27, 0.27)\", \"threshold2Color\": \"rgba(234, 112, 112, 0.22)\" }, \"lines\": true, \"fill\": 0, \"linewidth\": 1, \"points\": false, \"pointradius\": 5, \"bars\": false, \"stack\": false, \"percentage\": false, \"legend\": { \"show\": true, \"values\": false, \"min\": false, \"max\": false, \"current\": false, \"total\": false, \"avg\": false }, \"nullPointMode\": \"connected\", \"steppedLine\": false, \"tooltip\": { \"value_type\": \"cumulative\", \"shared\": false }, \"targets\": [ { \"function\": \"mean\", \"column\": \"value\", \"series\": \""
                            + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[1] + "\", \"query\": \"select * from \\\"" + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[1] + "\\\" where $timeFilter\", \"rawQuery\": true } ], \"aliasColors\": {}, \"seriesOverrides\": [] }, { \"title\": \"" + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[2] + "\", \"error\": false, \"span\": 4, \"editable\": true, \"type\": \"graph\", \"id\": ");
            jason.append(counter3);
            jason.append(
                    ", \"datasource\": null, \"renderer\": \"flot\", \"x-axis\": true, \"y-axis\": true, \"y_formats\": [ \"short\", \"short\" ], \"grid\": { \"leftMax\": null, \"rightMax\": null, \"leftMin\": null, \"rightMin\": null, \"threshold1\": null, \"threshold2\": null, \"threshold1Color\": \"rgba(216, 200, 27, 0.27)\", \"threshold2Color\": \"rgba(234, 112, 112, 0.22)\" }, \"lines\": true, \"fill\": 0, \"linewidth\": 1, \"points\": false, \"pointradius\": 5, \"bars\": false, \"stack\": false, \"percentage\": false, \"legend\": { \"show\": true, \"values\": false, \"min\": false, \"max\": false, \"current\": false, \"total\": false, \"avg\": false }, \"nullPointMode\": \"connected\", \"steppedLine\": false, \"tooltip\": { \"value_type\": \"cumulative\", \"shared\": false }, \"targets\": [ { \"function\": \"mean\", \"column\": \"value\", \"series\": \""
                            + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[2] + "\", \"query\": \"select * from \\\"" + rootPrefix + "");
            jason.append(columnName);
            jason.append("." + individualMetrics[2] + "\\\" where $timeFilter\", \"rawQuery\": true } ], \"aliasColors\": {}, \"seriesOverrides\": [] } ");
            jason.append("      ]\n" +
                    "    }");
            final int isLastNode = i + 1;

            if (isLastNode < metrics.size()) {

                jason.append(",\n");
            }

            // System.out.println(jason);
            counter = counter3 + 1;
            counter2 = counter3 + 2;
            counter3 = counter3 + 3;

        }

        final String tail = "  ],\n" +
                "  \"nav\": [\n" +
                "    {\n" +
                "      \"type\": \"timepicker\",\n" +
                "      \"enable\": true,\n" +
                "      \"status\": \"Stable\",\n" +
                "      \"time_options\": [\n" +
                "        \"5m\",\n" +
                "        \"15m\",\n" +
                "        \"1h\",\n" +
                "        \"6h\",\n" +
                "        \"12h\",\n" +
                "        \"24h\",\n" +
                "        \"2d\",\n" +
                "        \"7d\",\n" +
                "        \"30d\"\n" +
                "      ],\n" +
                "      \"refresh_intervals\": [\n" +
                "        \"5s\",\n" +
                "        \"10s\",\n" +
                "        \"30s\",\n" +
                "        \"1m\",\n" +
                "        \"5m\",\n" +
                "        \"15m\",\n" +
                "        \"30m\",\n" +
                "        \"1h\",\n" +
                "        \"2h\",\n" +
                "        \"1d\"\n" +
                "      ],\n" +
                "      \"now\": true,\n" +
                "      \"collapse\": false,\n" +
                "      \"notice\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"time\": {\n" +
                "    \"from\": \"now-5m\",\n" +
                "    \"to\": \"now\"\n" +
                "  },\n" +
                "  \"templating\": {\n" +
                "    \"list\": []\n" +
                "  },\n" +
                "  \"annotations\": {\n" +
                "    \"list\": []\n" +
                "  },\n" +
                "  \"refresh\": false,\n" +
                "  \"version\": 6,\n" +
                "  \"hideAllLegends\": false\n" +
                "}";
        jason.append(tail);

        PrintWriter writer = null;

        try {

            final String outputFile = outputFilePath + outputFileName + ".json";

            writer = new PrintWriter(outputFile, "UTF-8");
            writer.println(jason);
            writer.close();
            System.out.println("Dashboard file was successfully generated : "
                    + outputFile);
        } catch (final FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            writer.close();
            outputFilePathScanner.close();
            outputFileNameScanner.close();
            reader.close();
            scriptFileScanner.close();
            rootPrefixScanner.close();

        }

    }
}
