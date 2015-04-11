Welcome to the Grafana-Dashboard-Generator wiki!

This app lets you automatically generate cumulative metrics and individual metrics for any Jmeter script. The app automatically parses the script and creates necessary JSON data objects in the file to create the dashboard JSON file.

**The following metrics are included for each individual HTTPSampler:**<br>
1. percentile90 (response time)<br>
2. failure (transactions failed per interval)<br>
3. success (transactions passed per interval)<br>

**The following metrics are included for the cumulative metric group:**<br>
1. percentile90 (response time per interval)<br>
2. meanActiveThreads<br>
3. TotalTPS (Sum of Transactions Per Second of all transactions in the test plan per interval)<br>
4. failure (Sum of failures in the test per interval);<br>

**How to use the app:**<br>
1. Download the GenerateDashboard.jar file<br>
2. Copy it into any directory.<br>

**Run the command as following:**<br>
`java -jar GenerateDashboard.jar`<br>
`1.Enter a name for the Grafana dashboard:`<br>
AuthIDService<br>
`2.Enter output file path (Example: /var/tmp/ on Linux OR like C:\tmp\output\ on Windows):`<br>
/var/tmp/<br>
`3.Enter path of script(example: /var/tmp/test.jmx in Linux OR like C:\tmp\test.jmx in Windows):`<br>
/Jmeter/ius-prf-tax-medium-risk.jmx<br>
Selected Script = /Jmeter/ius-prf-tax-medium-risk.jmx<br>
Parsing the Jmeter script and looking for all HTTP transactions...<br>
The dashboard will contain the following transactions<br>
1.signInTTO<br>
2.createSignInConfirmation<br>
3.verifySignInConfirmation<br>
4.getAttribute<br>
5.updateAttribute<br>
6.lookupUserById<br>
7.lookupUserByName<br>
8.createUser<br>
9.signInTTO<br>
10.lookupGrant<br>
11.lookupGrant<br>
12.realmPicker<br>
13.lookupGrant<br>
14.signin<br>
15.lookupUserById<br>
16.lookupUserById<br>
17.lookupUserById<br>
18.getAssurnaceLevel<br>
19.accountRecovery<br>
20.signinAccountRecovery<br>
21.updateTktAssuranceLevel<br>
22.signinWithUserId<br>
23.lookupOfferingUsages<br>
24.signin<br>
25.getAttribute<br>
26.updateAttribute<br>
27.signInTTO_Bad<br>
28.signinLookupTaxId<br>
29.lookupTAXIdentifier<br>
Dashboard file successfully generated /var/tmp/AuthIDService.jason

**_Once the file is generated, launch grafana (like: localhost:<blahPort>/Grafana/) and click the "Import" button in the Dashboard dropdown. Select the generated JSON file and enjoy!!_**

![image](https://cloud.githubusercontent.com/assets/7585681/7100497/f79678b8-dfd9-11e4-8a29-7da4ad7e814a.png)

![image](https://cloud.githubusercontent.com/assets/7585681/7100648/98c52d44-dfe2-11e4-9eb9-56a39026131f.png)
