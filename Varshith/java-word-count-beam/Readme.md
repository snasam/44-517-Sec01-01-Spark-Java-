# Varshith Reddy Bairy

## initialized project using below command in powershell
-- PS> `mvn archetype:generate  -D archetypeGroupId=org.apache.beam -D archetypeArtifactId=beam-sdks-java-maven-archetypes-examples -D archetypeVersion=2.36.0  -D groupId=org.example -D artifactId=word-count-beam  -D version="0.1"  -D package=org.apache.beam.examples  -D interactiveMode=false`

## Create a sample text file
 I have created a sample text file in the root folder and added the content from Shakesphere sonnets into it

## Run a pipeline

Use the command in git bash
`$ mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.WordCount -Dexec.args="--inputFile=sample.txt --output=counts" -Pdirect-runner`


mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.MinimalPageRank -Dexec.args="--inputFile=sample.txt --output=counts" -Pdirect-runner

mvn compile exec:java -D exec.mainClass=edu.nwmissouri.SpectacularSix.MinimalPageRankVarshith

## Inspect the results
 `ls counts*`

 
