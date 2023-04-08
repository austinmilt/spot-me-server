# spotme-server
Recommends products based on genres of your spotify songs.


## Running a function
First, set the function under `<functionTarget>` you want to run in pom.xml in this section
to the class path of the function

```
<plugin>
    <groupId>com.google.cloud.functions</groupId>
    <artifactId>function-maven-plugin</artifactId>
    <version>0.10.1</version>
    <configuration>
        <functionTarget>com.austinmilt.spotme.HelloWorld</functionTarget>
    </configuration>
</plugin>
```


Next, define the following environment variables (or get a shell script from me to run):
```
OPEN_AI_API_KEY
```

Then run `mvn function:run`


## Deploying a function

First, get environment variables yaml (.env.yaml).

Next, build the jar with `mvn package` and then run

```
gcloud functions deploy spot-me-recommendations --env-vars-file .env.yaml --entry-point com.austinmilt.spotme.RecommendedProductsHandler --runtime java11 --trigger-http --memory 512MB --project wgmi-cc
```

Replace `spot-me-hello-world` with the name the deployed function should have,
and `com.austinmilt.spotme.HelloWorld` with the class path of the function.


## Resources
Functions framework: https://github.com/GoogleCloudPlatform/functions-framework-java
