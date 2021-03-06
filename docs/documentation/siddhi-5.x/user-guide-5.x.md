# Siddhi 5.x User Guide

## System Requirements
1. **Memory**   - 128 MB (minimum), 500 MB (recommended), higher memory might be needed based on in-memory data stored for processing
2. **Cores**    - 2 cores (recommended), use lower number of cores after testing Siddhi Apps for performance
3. **JDK**      - 8 or 11
4. To build Siddhi from the Source distribution, it is necessary that you have JDK version 8 or 11 and Maven 3.0.4 or later

## Using Siddhi in various environments

### **Using Siddhi as a Java library**

Siddhi can be used as a library in any Java program (including OSGi runtimes) just by adding Siddhi and its extension jars as dependencies.

* Find a sample Siddhi project that's implemented as a Java program using Maven [here](https://github.com/suhothayan/siddhi-sample), this can be used as a reference for any based implementation.

* Following are the mandatory dependencies that need to be added to the Maven `pom.xml` file (or to the class path).

```xml
   <dependency>
     <groupId>io.siddhi</groupId>
     <artifactId>siddhi-core</artifactId>
     <version>5.x.x</version>
   </dependency>
   <dependency>
     <groupId>io.siddhi</groupId>
     <artifactId>siddhi-query-api</artifactId>
     <version>5.x.x</version>
   </dependency>
   <dependency>
     <groupId>io.siddhi</groupId>
     <artifactId>siddhi-query-compiler</artifactId>
     <version>5.x.x</version>
   </dependency>
   <dependency>
     <groupId>io.siddhi</groupId>
     <artifactId>siddhi-annotations</artifactId>
     <version>5.x.x</version>
   </dependency>   
```

* Sample Java class using Siddhi is as follows.

<script src="https://gist.github.com/suhothayan/0c81a843101601e82a7175b7add2aa4d.js"></script>

### **Using Siddhi as Local Micro Service**

Siddhi can also run a standalone program by passing the SiddhiApps and required configurations to it.

* Download the the latest Siddhi distribution from [Github releases](https://github.com/siddhi-io/distribution/releases).
* Unzip the siddhi-runner-x.x.x.zip
* Start SiddhiApps with the runner config by executing the following commands from the distribution directory<br/>
  Linux/Mac : `./bin/runner.sh -Dapps=<siddhi-file> -Dconfig=<config-yaml-file>`<br/>
  Windows : `bin\runner.bat -Dapps=<siddhi-file> -Dconfig=<config-yaml-file>`

!!! Tip "Running Multiple SiddhiApps in one runner."
    To run multiple SiddhiApps in one runtime, have all SiddhiApps in a directory and pass its location through `-Dapps` parameter as follows,<br/>
    `-Dapps=<siddhi-apps-directory>`

!!! Note "Always use **absolute path** for SiddhiApps and runner configs."
    Providing absolute path of SiddhiApp file, or directory in `-Dapps` parameter, and when providing the Siddhi runner config yaml on `-Dconfig` parameter while starting Siddhi runner.


**Samples**

####Running Siddhi App

Following SiddhiApp collects events via HTTP and logs the number of events arrived during last 15 seconds.  

<script src="https://gist.github.com/suhothayan/8b0471cbe86a0089db5d6afb1c46f765.js"></script>

<ul>
    <li>Copy the above SiddhiApp, and create the SiddhiApp file <code>CountOverTime.siddhi</code>.</li>
    <li>Run the SiddhiApp by executing following commands from the distribution directory
        <ul>
            <li>Linux/Mac :
            <pre style="white-space:pre-wrap;">./bin/runner.sh -Dapps=&lt;absolute-siddhi-file-path&gt;/CountOverTime.siddhi</pre>
            </li>
            <li>Windows :
            <pre style="white-space:pre-wrap;">bin\runner.bat -Dapps=&lt;absolute-siddhi-file-path&gt;\CountOverTime.siddhi</pre>
            </li>
        </ul>
    </li>
    <li>Test the SiddhiApp by calling the HTTP endpoint using curl or Postman as follows
        <ul>
            <li><b>Publish events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST http://localhost:8006/production \
  --header "Content-Type:application/json" \
  -d '{"event":{"name":"Cake","amount":20.12}}'
</pre>
            </li>
            <li><b>Publish events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'http://localhost:8006/production' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "event": {
    "name": "Cake",
    "amount": 20.12
  }
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>Runner logs the total count on the console. Note, how the count increments with every event sent.
<pre style="white-space:pre-wrap;">
[2019-04-11 13:36:03,517]  INFO {io.siddhi.core.stream.output.sink.LogSink} - CountOverTime : TotalCountStream : Event{timestamp=1554969963512, data=[1], isExpired=false}
[2019-04-11 13:36:10,267]  INFO {io.siddhi.core.stream.output.sink.LogSink} - CountOverTime : TotalCountStream : Event{timestamp=1554969970267, data=[2], isExpired=false}
[2019-04-11 13:36:41,694]  INFO {io.siddhi.core.stream.output.sink.LogSink} - CountOverTime : TotalCountStream : Event{timestamp=1554970001694, data=[1], isExpired=false}
</pre>
    </li>
</ul>

####Running with runner config
When running SiddhiApps users can optionally provide a config yaml to Siddhi runner to manage configurations such as state persistence, databases connections and secure vault.

Following SiddhiApp collects events via HTTP and store them in H2 Database.

<script src="https://gist.github.com/suhothayan/2413a6f886219befe736bf4447af02de.js"></script>

The runner config can by configured with the relevant datasource information and passed when starting the runner

<script src="https://gist.github.com/suhothayan/015ae003d4ba8d4aeaadc19e4c9516fd.js"></script>

<ul>
    <li>Copy the above SiddhiApp, & config yaml, and create corresponding the SiddhiApp file <code>ConsumeAndStore.siddhi</code> and <code>TestDb.yaml</code> files.</li>
    <li>Run the SiddhiApp by executing following commands from the distribution directory
        <ul>
            <li>Linux/Mac :
            <pre style="white-space:pre-wrap;">./bin/runner.sh -Dapps=&lt;absolute-siddhi-file-path&gt;/ConsumeAndStore.siddhi \
  -Dconfig=&lt;absolute-config-yaml-path&gt;/TestDb.yaml</pre>
            </li>
            <li>Windows :
            <pre style="white-space:pre-wrap;">bin\runner.sh -Dapps=&lt;absolute-siddhi-file-path&gt;\ConsumeAndStore.siddhi ^
  -Dconfig=&lt;absolute-config-yaml-path&gt;\TestDb.yaml</pre>
            </li>
        </ul>
    </li>
    <li>Test the SiddhiApp by calling the HTTP endpoint using curl or Postman as follows
        <ul>
            <li><b>Publish events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST http://localhost:8006/production \
  --header "Content-Type:application/json" \
  -d '{"event":{"name":"Cake","amount":20.12}}'
</pre>
            </li>
            <li><b>Publish events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'http://localhost:8006/production' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "event": {
    "name": "Cake",
    "amount": 20.12
  }
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>Query Siddhi Store APIs to retrieve 10 records from the table.
        <ul>
            <li><b>Query stored events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST https://localhost:9443/stores/query \
  -H "content-type: application/json" \
  -u "admin:admin" \
  -d '{"appName" : "ConsumeAndStore", "query" : "from ProductionTable select * limit 10;" }' -k
</pre>
            </li>
            <li><b>Query stored events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'https://localhost:9443/stores/query' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "appName" : "ConsumeAndStore",
  "query" : "from ProductionTable select * limit 10;"
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>The results of the query will be as follows,
<pre style="white-space:pre-wrap;">
{
  "records":[
    ["Cake",20.12]
  ]
}</pre>
    </li>
</ul>

####Running with environmental/system variables
Templating SiddhiApps allows users to provide environment/system variables to siddhiApps at runtime. This can help users to migrate SiddhiApps from one environment to another (E.g from dev, test and to prod).

Following templated SiddhiApp collects events via HTTP, filters them based on `amount` greater than a given threshold value, and only sends the filtered events via email.

Here the `THRESHOLD` value, and `TO_EMAIL` are templated in the `TemplatedFilterAndEmail.siddhi` SiddhiApp.

<script src="https://gist.github.com/suhothayan/47b6300f629463e63b2d5be78d6e45b4.js"></script>

The runner config is configured with a gmail account to send email messages in `EmailConfig.yaml` by templating sending `EMAIL_ADDRESS`, `EMAIL_USERNAME` and `EMAIL_PASSWORD`.   

<script src="https://gist.github.com/suhothayan/3f36f8827d379925a8cca68dd78b9a8e.js"></script>

<ul>
    <li>Copy the above SiddhiApp, & config yaml, and create corresponding the SiddhiApp file <code>TemplatedFilterAndEmail.siddhi</code> and <code>EmailConfig.yaml</code> files.</li>
    
    <li>Set environment variables by running following in the termial Siddhi is about to run: 
         <pre style="white-space:pre-wrap;">
export THRESHOLD=20
export TO_EMAIL=&lt;to email address&gt; 
export EMAIL_ADDRESS=&lt;gmail address&gt;
export EMAIL_USERNAME=&lt;gmail username&gt;
export EMAIL_PASSWORD=&lt;gmail password&gt;</pre>
        Or they can also be passed as system variables by adding 
        <pre style="white-space:pre-wrap;">-DTHRESHOLD=20 -DTO_EMAIL=&gt;to email address&gt; -DEMAIL_ADDRESS=&lt;gmail address&gt; 
 -DEMAIL_USERNAME=&lt;gmail username&gt; -DEMAIL_PASSWORD=&lt;gmail password&gt;</pre>
        to the end of the runner startup script.
    </li>
        <li>Run the SiddhiApp by executing following commands from the distribution directory
        <ul>
            <li>Linux/Mac :
            <pre style="white-space:pre-wrap;">
./bin/runner.sh -Dapps=&lt;absolute-file-path&gt;/TemplatedFilterAndEmail.siddhi \
  -Dconfig=&lt;absolute-config-yaml-path&gt;/EmailConfig.yaml</pre>
            </li>
            <li>Windows :
            <pre style="white-space:pre-wrap;">
bin\runner.bat -Dapps=&lt;absolute-file-path&gt;\TemplatedFilterAndEmail.siddhi ^
  -Dconfig=&lt;absolute-config-yaml-path&gt;\EmailConfig.yaml</pre>
            </li>
        </ul>
    </li>
    <li>Test the SiddhiApp by calling the HTTP endpoint using curl or Postman as follows
        <ul>
            <li><b>Publish events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST http://localhost:8006/production \
  --header "Content-Type:application/json" \
  -d '{"event":{"name":"Cake","amount":2000.0}}'
</pre>
            </li>
            <li><b>Publish events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'http://localhost:8006/production' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "event": {
    "name": "Cake",
    "amount": 2000.0
  }
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>Check the <code>to.email</code> for the published email message, which will look as follows,
<pre style="white-space:pre-wrap;">
Subject : High Cake production!

Hi, 

High production of Cake, with amount 2000.0 identified. 

For more information please contact production department. 

Thank you</pre>
    </li>
</ul>

### **Using Siddhi as Docker Micro Service**

Siddhi can also run on Docker by passing the SiddhiApps and required configurations to it.

* Pull the the latest Siddhi Runner image from [Docker Hub](https://hub.docker.com/).
```
docker pull siddhiio/siddhi-runner-alpine:latest
```
* Start SiddhiApps with the runner config by executing the following docker command.<br/>
```
docker run -it -v <local-siddhi-file-path>:<siddhi-file-mount-path> -v <local-conf-file-path>:<conf-file-mount-path> siddhiio/siddhi-runner-alpine:latest -Dapps=<siddhi-file-mount-path> -Dconfig=<conf-file-mount-path>
```
E.g.,
```
docker run -it -v /home/me/siddhi-apps:/apps -v /home/me/siddhi-configs:/configs siddhiio/siddhi-runner-alpine:latest -Dapps=/apps/Foo.siddhi -Dconfig=/configs/siddhi-config.yaml
```

!!! Tip "Running multiple SiddhiApps in one runner instance."
    To run multiple SiddhiApps in one runtime instance, have all SiddhiApps in a directory, mount the directory and pass its location through `-Dapps` parameter as follows,<br/>
    `-Dapps=<siddhi-apps-directory>`

!!! Note "Always use **absolute path** for SiddhiApps and runner configs."
    Providing absolute path of SiddhiApp file, or directory in `-Dapps` parameter, and when providing the Siddhi runner config yaml on `-Dconfig` parameter while starting Siddhi runner.


**Samples**

####Running Siddhi App

Following SiddhiApp collects events via HTTP and logs the number of events arrived during last 15 seconds.  

<script src="https://gist.github.com/suhothayan/8b0471cbe86a0089db5d6afb1c46f765.js"></script>

<ul>
    <li>Copy the above SiddhiApp, and create the SiddhiApp file <code>CountOverTime.siddhi</code>.</li>
    <li>Run the SiddhiApp by executing following commands from the distribution directory
        <ul>
            <li>
            <pre style="white-space:pre-wrap;">docker run -it -p 8006:8006 -v &lt;local-absolute-siddhi-file-path&gt;/CountOverTime.siddhi:/apps/CountOverTime.siddhi siddhiio/siddhi-runner-alpine -Dapps=/apps/CountOverTime.siddhi
</pre>
            </li>
        </ul>
    </li>
    <li>Test the SiddhiApp by calling the HTTP endpoint using curl or Postman as follows
        <ul>
            <li><b>Publish events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST http://localhost:8006/production \
  --header "Content-Type:application/json" \
  -d '{"event":{"name":"Cake","amount":20.12}}'
</pre>
            </li>
            <li><b>Publish events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'http://localhost:8006/production' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "event": {
    "name": "Cake",
    "amount": 20.12
  }
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>Runner logs the total count on the console. Note, how the count increments with every event sent.
<pre style="white-space:pre-wrap;">
[2019-04-11 13:36:03,517]  INFO {io.siddhi.core.stream.output.sink.LogSink} - CountOverTime : TotalCountStream : Event{timestamp=1554969963512, data=[1], isExpired=false}
[2019-04-11 13:36:10,267]  INFO {io.siddhi.core.stream.output.sink.LogSink} - CountOverTime : TotalCountStream : Event{timestamp=1554969970267, data=[2], isExpired=false}
[2019-04-11 13:36:41,694]  INFO {io.siddhi.core.stream.output.sink.LogSink} - CountOverTime : TotalCountStream : Event{timestamp=1554970001694, data=[1], isExpired=false}
</pre>
    </li>
</ul>

####Running with runner config
When running SiddhiApps users can optionally provide a config yaml to Siddhi runner to manage configurations such as state persistence, databases connections and secure vault.

Following SiddhiApp collects events via HTTP and store them in H2 Database.

<script src="https://gist.github.com/suhothayan/2413a6f886219befe736bf4447af02de.js"></script>

The runner config can be configured with the relevant datasource information and passed when starting the runner

<script src="https://gist.github.com/suhothayan/015ae003d4ba8d4aeaadc19e4c9516fd.js"></script>

<ul>
    <li>Copy the above SiddhiApp, & config yaml, and create corresponding the SiddhiApp file <code>ConsumeAndStore.siddhi</code> and <code>TestDb.yaml</code> files.</li>
    <li>Run the SiddhiApp by executing following command
        <ul>
            <li>
             <pre style="white-space:pre-wrap;">docker run -it -p 8006:8006 -p 9443:9443 -v &lt;local-absolute-siddhi-file-path&gt;/ConsumeAndStore.siddhi:/apps/ConsumeAndStore.siddhi -v &lt;local-absolute-config-yaml-path&gt;/TestDb.yaml:/conf/TestDb.yaml siddhiio/siddhi-runner-alpine -Dapps=/apps/ConsumeAndStore.siddhi -Dconfig=/conf/TestDb.yaml</pre>
            </li>
        </ul>
    </li>
    <li>Test the SiddhiApp by calling the HTTP endpoint using curl or Postman as follows
        <ul>
            <li><b>Publish events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST http://localhost:8006/production \
  --header "Content-Type:application/json" \
  -d '{"event":{"name":"Cake","amount":20.12}}'
</pre>
            </li>
            <li><b>Publish events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'http://localhost:8006/production' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "event": {
    "name": "Cake",
    "amount": 20.12
  }
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>Query Siddhi Store APIs to retrieve 10 records from the table.
        <ul>
            <li><b>Query stored events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST https://localhost:9443/stores/query \
  -H "content-type: application/json" \
  -u "admin:admin" \
  -d '{"appName" : "ConsumeAndStore", "query" : "from ProductionTable select * limit 10;" }' -k
</pre>
            </li>
            <li><b>Query stored events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'https://localhost:9443/stores/query' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "appName" : "ConsumeAndStore",
  "query" : "from ProductionTable select * limit 10;"
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>The results of the query will be as follows,
<pre style="white-space:pre-wrap;">
{
  "records":[
    ["Cake",20.12]
  ]
}</pre>
    </li>
</ul>

####Running with environmental/system variables
Templating SiddhiApps allows users to provide environment/system variables to siddhiApps at runtime. This can help users to migrate SiddhiApps from one environment to another (E.g from dev, test and to prod).

Following templated SiddhiApp collects events via HTTP, filters them based on `amount` greater than a given threshold value, and only sends the filtered events via email.

Here the `THRESHOLD` value, and `TO_EMAIL` are templated in the `TemplatedFilterAndEmail.siddhi` SiddhiApp.

<script src="https://gist.github.com/suhothayan/47b6300f629463e63b2d5be78d6e45b4.js"></script>

The runner config is configured with a gmail account to send email messages in `EmailConfig.yaml` by templating sending `EMAIL_ADDRESS`, `EMAIL_USERNAME` and `EMAIL_PASSWORD`.   

<script src="https://gist.github.com/suhothayan/3f36f8827d379925a8cca68dd78b9a8e.js"></script>

<ul>
    <li>Copy the above SiddhiApp, & config yaml, and create corresponding the SiddhiApp file <code>TemplatedFilterAndEmail.siddhi</code> and <code>EmailConfig.yaml</code> files.</li>
    
    <li>Set the below environment variables by passing them during the docker run command: 
         <pre style="white-space:pre-wrap;">
 THRESHOLD=20
 TO_EMAIL=&lt;to email address&gt; 
 EMAIL_ADDRESS=&lt;gmail address&gt;
 EMAIL_USERNAME=&lt;gmail username&gt;
 EMAIL_PASSWORD=&lt;gmail password&gt;</pre>
        Or they can also be passed as system variables by adding them to the end of the docker run command .
        <pre style="white-space:pre-wrap;">-DTHRESHOLD=20 -DTO_EMAIL=&gt;to email address&gt; -DEMAIL_ADDRESS=&lt;gmail address&gt; 
 -DEMAIL_USERNAME=&lt;gmail username&gt; -DEMAIL_PASSWORD=&lt;gmail password&gt;</pre>

    </li>
        <li>Run the SiddhiApp by executing following command.
        <ul>
            <li>
            <pre style="white-space:pre-wrap;">docker run -it -p 8006:8006 -v &lt;local-absolute-siddhi-file-path&gt;/TemplatedFilterAndEmail.siddhi:/apps/TemplatedFilterAndEmail.siddhi -v &lt;local-absolute-config-yaml-path&gt;/EmailConfig.yaml:/conf/EmailConfig.yaml -e THRESHOLD=20 -e TO_EMAIL=&lt;to email address&gt; -e EMAIL_ADDRESS=&lt;gmail address&gt; -e EMAIL_USERNAME=&lt;gmail username&gt; -e EMAIL_PASSWORD=&lt;gmail password&gt; siddhiio/siddhi-runner-alpine -Dapps=/apps/TemplatedFilterAndEmail.siddhi -Dconfig=/conf/EmailConfig.yaml </pre>

            </li>
        </ul>
    </li>
    <li>Test the SiddhiApp by calling the HTTP endpoint using curl or Postman as follows
        <ul>
            <li><b>Publish events with curl command:</b><br/>
            Publish few json to the http endpoint as follows,
<pre style="white-space:pre-wrap;">
curl -X POST http://localhost:8006/production \
  --header "Content-Type:application/json" \
  -d '{"event":{"name":"Cake","amount":2000.0}}'
</pre>
            </li>
            <li><b>Publish events with Postman:</b>
              <ol>
                <li>Install 'Postman' application from Chrome web store</li>
                <li>Launch the application</li>
                <li>Make a 'Post' request to 'http://localhost:8006/production' endpoint. Set the Content-Type to <code>'application/json'</code> and set the request body in json format as follows,
<pre>
{
  "event": {
    "name": "Cake",
    "amount": 2000.0
  }
}</pre>
                </li>
              </ol>
            </li>
        </ul>
    </li>
    <li>Check the <code>to.email</code> for the published email message, which will look as follows,
<pre style="white-space:pre-wrap;">
Subject : High Cake production!

Hi, 

High production of Cake, with amount 2000.0 identified. 

For more information please contact production department. 

Thank you</pre>
    </li>
</ul>




### **Using Siddhi as Kubernetes Micro Service**

#### Prerequisites 
1. First, you need a kubernetes cluster to deploy and run siddhi applications.
   1. [minikube](https://github.com/kubernetes/minikube#installation)
   2. [Google Kubernetes Engine(GKE) Cluster](https://console.cloud.google.com/)

##### Google Kubernetes Engine(GKE) Cluster
If you are running on a GKE, before installing the siddhi operator, you have to give cluster admin permission to your account. In order to do that execute the following command (replace "your-address@gmail.com" with your account email address). 

```    
kubectl create clusterrolebinding user-cluster-admin-binding --clusterrole=cluster-admin --user=your-address@gmail.com
```  
    
##### Minikube
If you are running on minikube you have to enable ingress using the following command.

```
minikube addons enable ingress
```
    

#### Install the siddhi operator
To install the siddhi kubernetes operator use the following commands.

```
git clone https://github.com/siddhi-io/siddhi-operator.git
cd siddhi-operator
kubectl create -f ./deploy/crds/siddhi_v1alpha1_siddhiprocess_crd.yaml
kubectl create -f ./deploy/service_account.yaml
kubectl create -f ./deploy/role.yaml
kubectl create -f ./deploy/role_binding.yaml
kubectl create -f ./deploy/operator.yaml
```

After the execution of these commands, you will see the following deployments in your kubernetes cluster if the siddhi operator deployed successfully.

```
$ kubectl get deploy
NAME              DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
siddhi-operator   1         1         1            1           1m
siddhi-parser     1         1         1            1           1m

```

#### Deploy and run a siddhi application
Now you can deploy and run siddhi applications using the deployed siddhi operator. In order to deploy siddhi files, you need custom object YAML file like below.

<script src="https://gist.github.com/BuddhiWathsala/f029d671f4f6d7719dce59500b970815.js"></script>

Copy this YAML file to a file name `monitor-app.yaml` and execute the following command to install the siddhi app in your cluster.

```
kubectl create -f <absolute-yaml-file-path>/monitor-app.yaml
```

If the monitor siddhi app deployed successfully you can see the created deployment, service, and ingress in your cluster as follow.

```
$ kubectl get deploy
NAME              DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
monitor-app       1         1         1            1           1m
siddhi-operator   1         1         1            1           1m
siddhi-parser     1         1         1            1           1m

$ kubectl get svc
NAME              TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes        ClusterIP      10.96.0.1        <none>        443/TCP          10d
monitor-app       ClusterIP      10.101.242.132   <none>        8280/TCP         1m
siddhi-operator   ClusterIP      10.111.138.250   <none>        8383/TCP         1m
siddhi-parser     LoadBalancer   10.102.172.142   <pending>     9090:31830/TCP   1m

$ kubectl get ing
NAME      HOSTS     ADDRESS     PORTS     AGE
siddhi    siddhi    10.0.2.15   80, 443   1m

```
Obtain the external IP of the ingress load balancer using `kubectl get ing` and add the host `siddhi` along with that external IP as an entry to the `/etc/hosts` file.

!!! Note "If you are using `minikube`, use minikube IP as the external IP."
    Use `minikube ip` command to get the IP of the minikube cluster.

Now you can use following CURL command to send events to your deployed siddhi application.

```
curl -X POST \
  http://siddhi/monitor-app/8280/example \
  -H 'Content-Type: application/json' \
  -d '{
	"type": "monitored",
	"deviceID": "001",
	"power": 341
}'

```

#### Monitoring the status

##### Listing the siddhi processes

You can use `sps or SiddhiProcesses` names to list the `SiddhiProcess` custom objects.

```
$ kubectl get sps
NAME          AGE
monitor-app   2m

$ kubectl get SiddhiProcesses
NAME          AGE
monitor-app   2m
```

##### View siddhi process configs

You can view the configuration details of the deployed siddhi processes using `kubectl describe` and `kubectl get` as follow using `sp or SiddhiProcess` names.

```
$ kubectl describe sp monitor-app

Name:         monitor-app
Namespace:    default
Labels:       <none>
Annotations:  <none>
API Version:  siddhi.io/v1alpha1
Kind:         SiddhiProcess
Metadata:
  Creation Timestamp:  2019-04-18T18:05:39Z
  Generation:          1
  Resource Version:    497702
  Self Link:           /apis/siddhi.io/v1alpha1/namespaces/default/siddhiprocesses/monitor-app
  UID:                 92b2293b-6204-11e9-996c-0800279e6dba
Spec:
  Env:
    Name:   RECEIVER_URL
    Value:  http://0.0.0.0:8280/example
    Name:   BASIC_AUTH_ENABLED
    Value:  false
  Pod:
    Image:      siddhiio/siddhi-runner-alpine
    Image Tag:  0.1.0
  Query:        @App:name("MonitorApp")
@App:description("Description of the plan") 

@sink(type='log', prefix='LOGGER')
@source(type='http', receiver.url='${RECEIVER_URL}', basic.auth.enabled='${BASIC_AUTH_ENABLED}', @map(type='json'))
define stream DevicePowerStream (type string, deviceID string, power int);


define stream MonitorDevicesPowerStream(deviceID string, power int);

@info(name='monitored-filter')
from DevicePowerStream[type == 'monitored']
select deviceID, power
insert into MonitorDevicesPowerStream;

  Siddhi . Runner . Configs:  state.persistence:
  enabled: true
  intervalInMin: 5
  revisionsToKeep: 2
  persistenceStore: io.siddhi.distribution.core.persistence.FileSystemPersistenceStore
  config:
    location: siddhi-app-persistence

Status:
  Nodes:   <nil>
  Status:  Running
Events:    <none>

```

```
$ kubectl get sp monitor-app -o yaml

apiVersion: siddhi.io/v1alpha1
kind: SiddhiProcess
metadata:
  creationTimestamp: 2019-04-18T18:05:39Z
  generation: 1
  name: monitor-app
  namespace: default
  resourceVersion: "497702"
  selfLink: /apis/siddhi.io/v1alpha1/namespaces/default/siddhiprocesses/monitor-app
  uid: 92b2293b-6204-11e9-996c-0800279e6dba
spec:
  env:
  - name: RECEIVER_URL
    value: http://0.0.0.0:8280/example
  - name: BASIC_AUTH_ENABLED
    value: "false"
  pod:
    image: siddhiio/siddhi-runner-alpine
    imageTag: 0.1.0
  query: "@App:name(\"MonitorApp\")\n@App:description(\"Description of the plan\")
    \n\n@sink(type='log', prefix='LOGGER')\n@source(type='http', receiver.url='${RECEIVER_URL}',
    basic.auth.enabled='${BASIC_AUTH_ENABLED}', @map(type='json'))\ndefine stream
    DevicePowerStream (type string, deviceID string, power int);\n\n\ndefine stream
    MonitorDevicesPowerStream(deviceID string, power int);\n\n@info(name='monitored-filter')\nfrom
    DevicePowerStream[type == 'monitored']\nselect deviceID, power\ninsert into MonitorDevicesPowerStream;\n"
  siddhi.runner.configs: |
    state.persistence:
      enabled: true
      intervalInMin: 5
      revisionsToKeep: 2
      persistenceStore: io.siddhi.distribution.core.persistence.FileSystemPersistenceStore
      config:
        location: siddhi-app-persistence
status:
  nodes: null
  status: Running

```

##### View siddhi process logs

You can list the deployed pods.

```
$ kubectl get pods

NAME                               READY     STATUS    RESTARTS   AGE
monitor-app-7f8584875f-krz6t       1/1       Running   0          2m
siddhi-operator-8589c4fc69-6xbtx   1/1       Running   0          2m
siddhi-parser-64d4cd86ff-pfq2s     1/1       Running   0          2m
```

Then you can view the logs of that deployed pod. This log shows you the event that you sent using the previous POST request

```
$ kubectl logs monitor-app-7f8584875f-krz6t
JAVA_HOME environment variable is set to /opt/java/openjdk
CARBON_HOME environment variable is set to /home/siddhi_user/siddhi-runner-0.1.0
RUNTIME_HOME environment variable is set to /home/siddhi_user/siddhi-runner-0.1.0/wso2/runner
Picked up JAVA_TOOL_OPTIONS: -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap
[2019-04-20 3:58:57,734]  INFO {org.wso2.carbon.launcher.extensions.OSGiLibBundleDeployerUtils updateOSGiLib} - Successfully updated the OSGi bundle information of Carbon Runtime: runner  
osgi> [2019-04-20 03:59:00,208]  INFO {org.wso2.carbon.config.reader.ConfigFileReader} - Default deployment configuration updated with provided custom configuration file monitor-app-deployment.yaml
[2019-04-20 03:59:01,551]  INFO {org.wso2.msf4j.internal.websocket.WebSocketServerSC} - All required capabilities are available of WebSocket service component is available.
[2019-04-20 03:59:01,584]  INFO {org.wso2.carbon.metrics.core.config.model.JmxReporterConfig} - Creating JMX reporter for Metrics with domain 'org.wso2.carbon.metrics'
[2019-04-20 03:59:01,609]  INFO {org.wso2.msf4j.analytics.metrics.MetricsComponent} - Metrics Component is activated
[2019-04-20 03:59:01,614]  INFO {org.wso2.carbon.databridge.agent.internal.DataAgentDS} - Successfully deployed Agent Server 
[2019-04-20 03:59:02,219]  INFO {io.siddhi.distribution.core.internal.ServiceComponent} - Periodic state persistence started with an interval of 5 using io.siddhi.distribution.core.persistence.FileSystemPersistenceStore
[2019-04-20 03:59:02,229]  INFO {io.siddhi.distribution.event.simulator.core.service.CSVFileDeployer} - CSV file deployer initiated.
[2019-04-20 03:59:02,233]  INFO {io.siddhi.distribution.event.simulator.core.service.SimulationConfigDeployer} - Simulation config deployer initiated.
[2019-04-20 03:59:02,279]  INFO {org.wso2.carbon.databridge.receiver.binary.internal.BinaryDataReceiverServiceComponent} - org.wso2.carbon.databridge.receiver.binary.internal.Service Component is activated
[2019-04-20 03:59:02,312]  INFO {org.wso2.carbon.databridge.receiver.binary.internal.BinaryDataReceiver} - Started Binary SSL Transport on port : 9712
[2019-04-20 03:59:02,321]  INFO {org.wso2.carbon.databridge.receiver.binary.internal.BinaryDataReceiver} - Started Binary TCP Transport on port : 9612
[2019-04-20 03:59:02,322]  INFO {org.wso2.carbon.databridge.receiver.thrift.internal.ThriftDataReceiverDS} - Service Component is activated
[2019-04-20 03:59:02,344]  INFO {org.wso2.carbon.databridge.receiver.thrift.ThriftDataReceiver} - Thrift Server started at 0.0.0.0
[2019-04-20 03:59:02,356]  INFO {org.wso2.carbon.databridge.receiver.thrift.ThriftDataReceiver} - Thrift SSL port : 7711
[2019-04-20 03:59:02,363]  INFO {org.wso2.carbon.databridge.receiver.thrift.ThriftDataReceiver} - Thrift port : 7611
[2019-04-20 03:59:02,449]  INFO {org.wso2.msf4j.internal.MicroservicesServerSC} - All microservices are available
[2019-04-20 03:59:02,516]  INFO {org.wso2.transport.http.netty.contractimpl.listener.ServerConnectorBootstrap$HttpServerConnector} - HTTP(S) Interface starting on host 0.0.0.0 and port 9090
[2019-04-20 03:59:02,520]  INFO {org.wso2.transport.http.netty.contractimpl.listener.ServerConnectorBootstrap$HttpServerConnector} - HTTP(S) Interface starting on host 0.0.0.0 and port 9443
[2019-04-20 03:59:03,068]  INFO {io.siddhi.distribution.core.internal.StreamProcessorService} - Periodic State persistence enabled. Restoring last persisted state of MonitorApp
[2019-04-20 03:59:03,075]  INFO {org.wso2.transport.http.netty.contractimpl.listener.ServerConnectorBootstrap$HttpServerConnector} - HTTP(S) Interface starting on host 0.0.0.0 and port 8280
[2019-04-20 03:59:03,077]  INFO {org.wso2.extension.siddhi.io.http.source.HttpConnectorPortBindingListener} - HTTP source 0.0.0.0:8280 has been started
[2019-04-20 03:59:03,084]  INFO {io.siddhi.distribution.core.internal.StreamProcessorService} - Siddhi App MonitorApp deployed successfully
[2019-04-20 03:59:03,093]  INFO {org.wso2.carbon.kernel.internal.CarbonStartupHandler} - Siddhi Runner Distribution started in 5.941 sec
[2019-04-20 04:04:02,216]  INFO {org.wso2.extension.siddhi.io.http.source.HttpSourceListener} - Event input has paused for http://0.0.0.0:8280/example
[2019-04-20 04:04:02,235]  INFO {org.wso2.extension.siddhi.io.http.source.HttpSourceListener} - Event input has resume for http://0.0.0.0:8280/example
[2019-04-20 04:05:29,741]  INFO {io.siddhi.core.stream.output.sink.LogSink} - LOGGER : Event{timestamp=1555733129736, data=[monitored, 001, 341], isExpired=false}
```



### **Using Siddhi as a Python Library**

WIP
