package com.dashboard.generator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GenerateDashboard {
	public static void main(String[] args) {

		List<String> metrics = new ArrayList<String>();

		System.out.println("1.Enter a name for the Grafana dashboard:");
		Scanner outputFileNameScanner = new Scanner(System.in);
		String outputFileName = outputFileNameScanner.nextLine();

		System.out.println("2.Enter output file path (Example: /var/tmp/ on Linux OR like C:\\tmp\\output\\ on Windows):");
		Scanner outputFilePathScanner = new Scanner(System.in);
		String outputFilePath = outputFilePathScanner.nextLine();

		System.out.println("3.Enter path of script(example: /var/tmp/test.jmx in Linux OR like C:\\tmp\\test.jmx in Windows):");
		Scanner fileScan = new Scanner(System.in);

		String path = fileScan.nextLine();

		System.out.println("Selected Script = " + path);
		Scanner reader = null;
		if (path != null) {
			try {
				reader = new Scanner(new File(path));
			} catch (FileNotFoundException e) {
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

					int startIndex = tempLineReadString.indexOf("testname=\"");
					int endIndex = tempLineReadString.indexOf("\" enabled");
					String testPlanName = tempLineReadString.substring(
							startIndex + 10, endIndex);
					metrics.add(testPlanName);
					tempLineReadString = null;
					System.out.println(++lineReaderCount+"."+testPlanName);
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

		String[] aggregationMetric = { "meanActiveThreads", "percentile90",
				"failure", "success" };
		int iCounter = 0;
		int idCounter = 88;
		
		String head = "{\n" + 
				"  \"id\": null,\n" + 
				"  \"title\": \"IDManager\",\n" + 
				"  \"originalTitle\": \"IDManager\",\n" + 
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
			String object = "{\n" + 
					"          \"title\": \"Total Threads\",\n" + 
					"          \"error\": false,\n" + 
					"          \"span\": 6,\n" + 
					"          \"editable\": true,\n" + 
					"          \"type\": \"graph\",\n" + 
					"          \"id\": "+idCounter+",\n" + 
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
					"              \"query\": \"select * from jmeter.cumulated."+aggregationMetric[iCounter]+" where time > now() - 5m\"\n" + 
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
		// display to console
		for (int i = 0; i < metrics.size(); i++) {
			columnName = metrics.get(i);
			// System.out.println(columnName+"\n");

			rowName = "\"" + columnName + "\"";
			jason.append("{ \"title\": ");
			jason.append(rowName);
			jason.append(", \"height\": \"250px\", \"editable\": true, \"collapse\": false, \"panels\": [ { \"title\": \"jmeter.");
			jason.append(columnName);
			jason.append(".percentile90\", \"error\": false, \"span\": 4, \"editable\": true, \"type\": \"graph\", \"id\": ");
			jason.append(counter);
			jason.append(", \"datasource\": null, \"renderer\": \"flot\", \"x-axis\": true, \"y-axis\": true, \"y_formats\": [ \"short\", \"short\" ], \"grid\": { \"leftMax\": null, \"rightMax\": null, \"leftMin\": null, \"rightMin\": null, \"threshold1\": null, \"threshold2\": null, \"threshold1Color\": \"rgba(216, 200, 27, 0.27)\", \"threshold2Color\": \"rgba(234, 112, 112, 0.22)\" }, \"lines\": true, \"fill\": 0, \"linewidth\": 1, \"points\": false, \"pointradius\": 5, \"bars\": false, \"stack\": false, \"percentage\": false, \"legend\": { \"show\": true, \"values\": false, \"min\": false, \"max\": false, \"current\": false, \"total\": false, \"avg\": false }, \"nullPointMode\": \"connected\", \"steppedLine\": false, \"tooltip\": { \"value_type\": \"cumulative\", \"shared\": false }, \"targets\": [ { \"function\": \"mean\", \"column\": \"value\", \"series\": \"jmeter.");
			jason.append(columnName);
			jason.append(".percentile90\", \"query\": \"select * from  jmeter.");
			jason.append(columnName);
			jason.append(".percentile90 where time > now() - 1h\", \"rawQuery\": true } ], \"aliasColors\": {}, \"seriesOverrides\": [] }, { \"title\": \"jmeter.");
			jason.append(columnName);
			jason.append(".success\", \"error\": false, \"span\": 4, \"editable\": true, \"type\": \"graph\", \"id\": ");
			jason.append(counter2);
			jason.append(", \"datasource\": null, \"renderer\": \"flot\", \"x-axis\": true, \"y-axis\": true, \"y_formats\": [ \"short\", \"short\" ], \"grid\": { \"leftMax\": null, \"rightMax\": null, \"leftMin\": null, \"rightMin\": null, \"threshold1\": null, \"threshold2\": null, \"threshold1Color\": \"rgba(216, 200, 27, 0.27)\", \"threshold2Color\": \"rgba(234, 112, 112, 0.22)\" }, \"lines\": true, \"fill\": 0, \"linewidth\": 1, \"points\": false, \"pointradius\": 5, \"bars\": false, \"stack\": false, \"percentage\": false, \"legend\": { \"show\": true, \"values\": false, \"min\": false, \"max\": false, \"current\": false, \"total\": false, \"avg\": false }, \"nullPointMode\": \"connected\", \"steppedLine\": false, \"tooltip\": { \"value_type\": \"cumulative\", \"shared\": false }, \"targets\": [ { \"function\": \"mean\", \"column\": \"value\", \"series\": \"jmeter.");
			jason.append(columnName);
			jason.append(".success\", \"query\": \"select * from  jmeter.");
			jason.append(columnName);
			jason.append(".success where time > now() - 1h\", \"rawQuery\": true } ], \"aliasColors\": {}, \"seriesOverrides\": [] }, { \"title\": \"jmeter.");
			jason.append(columnName);
			jason.append(".failure\", \"error\": false, \"span\": 4, \"editable\": true, \"type\": \"graph\", \"id\": ");
			jason.append(counter3);
			jason.append(", \"datasource\": null, \"renderer\": \"flot\", \"x-axis\": true, \"y-axis\": true, \"y_formats\": [ \"short\", \"short\" ], \"grid\": { \"leftMax\": null, \"rightMax\": null, \"leftMin\": null, \"rightMin\": null, \"threshold1\": null, \"threshold2\": null, \"threshold1Color\": \"rgba(216, 200, 27, 0.27)\", \"threshold2Color\": \"rgba(234, 112, 112, 0.22)\" }, \"lines\": true, \"fill\": 0, \"linewidth\": 1, \"points\": false, \"pointradius\": 5, \"bars\": false, \"stack\": false, \"percentage\": false, \"legend\": { \"show\": true, \"values\": false, \"min\": false, \"max\": false, \"current\": false, \"total\": false, \"avg\": false }, \"nullPointMode\": \"connected\", \"steppedLine\": false, \"tooltip\": { \"value_type\": \"cumulative\", \"shared\": false }, \"targets\": [ { \"function\": \"mean\", \"column\": \"value\", \"series\": \"jmeter.");
			jason.append(columnName);
			jason.append(".percentile90\", \"query\": \"select * from  jmeter.");
			jason.append(columnName);
			jason.append(".failure where time > now() - 1h\", \"rawQuery\": true } ], \"aliasColors\": {}, \"seriesOverrides\": [] } ");
			jason.append("      ]\n" + 
					"    }");
			int isLastNode = i + 1;
		
			
			if (isLastNode < metrics.size()) {
				
				jason.append(",\n");
			}

			// System.out.println(jason);
			counter = counter3 + 1;
			counter2 = counter3 + 2;
			counter3 = counter3 + 3;

		}

		String tail = "  ],\n" + 
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
			
			
			String outputFile = outputFilePath + outputFileName + ".json";

			writer = new PrintWriter(outputFile, "UTF-8");
			writer.println(jason);
			writer.close();
			System.out.println("Dashboard file successfully generated "
					+ outputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
			outputFilePathScanner.close();
			outputFileNameScanner.close();
			reader.close();
			fileScan.close();

		}

	}
}
