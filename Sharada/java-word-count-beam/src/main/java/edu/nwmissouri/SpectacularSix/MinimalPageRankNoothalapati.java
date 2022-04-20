package edu.nwmissouri.SpectacularSix;

import java.util.ArrayList;
import java.util.Collection;

//import org.apache.beam.model.pipeline.v1.RunnerApi.PCollection;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.TypeDescriptors;

public class MinimalPageRankNoothalapati {

  static class Job1Finalizer extends DoFn<KV<String, Iterable<String>>, KV<String, SharadaRankedPage>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<String>> element,
        OutputReceiver<KV<String, SharadaRankedPage>> receiver) {
      Integer contributorVotes = 0;
      if (element.getValue() instanceof Collection) {
        contributorVotes = ((Collection<String>) element.getValue()).size();
      }
      ArrayList<SharadaVotingPage> voters = new ArrayList<SharadaVotingPage>();
      for (String voterName : element.getValue()) {
        if (!voterName.isEmpty()) {
          voters.add(new SharadaVotingPage(voterName, contributorVotes));
        }
      }
      receiver.output(KV.of(element.getKey(), new SharadaRankedPage(element.getKey(), voters)));
    }
  }

  static class Job2Mapper extends DoFn<KV<String, SharadaRankedPage>, KV<String, SharadaRankedPage>> {
  }

  static class Job2Updater extends DoFn<KV<String, Iterable<SharadaRankedPage>>, KV<String, SharadaRankedPage>> {
  }

  public static void main(String[] args) {

    PipelineOptions options = PipelineOptionsFactory.create();

    Pipeline p = Pipeline.create(options);

    String dataFolder = "web04";
    String dataFile = "go.md";
    String dataPath = dataFolder + "/" + dataFile;
    // p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV1 = SharadaFirstMapJob(p, dataFile, dataPath);

    dataFile = "java.md";
    dataPath = dataFolder + "/" + dataFile;
    // p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV2 = SharadaFirstMapJob(p, dataFile, dataPath);

    dataFile = "python.md";
    dataPath = dataFolder + "/" + dataFile;
    // p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV3 = SharadaFirstMapJob(p, dataFile, dataPath);

    dataFile = "README.md";
    dataPath = dataFolder + "/" + dataFile;
    // p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV4 = SharadaFirstMapJob(p, dataFile, dataPath);

    PCollectionList<KV<String, String>> myLst = PCollectionList.of(pckvLinksKV1).and(pckvLinksKV2).and(pckvLinksKV3)
        .and(pckvLinksKV4);

    PCollection<KV<String, String>> myMergeLst = myLst.apply(Flatten.<KV<String, String>>pCollections());

    PCollection<KV<String, Iterable<String>>> groupByLst = myMergeLst.apply(GroupByKey.<String, String>create());

    PCollection<String> pckvLinksStrings = groupByLst.apply(
        MapElements.into(
            TypeDescriptors.strings())
            .via((myMergeLstout) -> myMergeLstout.toString()));

    pckvLinksStrings.apply(TextIO.write().to("Sharadawordcounts"));

    p.run().waitUntilFinish();
  }

  private static PCollection<KV<String, String>> SharadaFirstMapJob(Pipeline p, String dataFile, String dataPath) {
    PCollection<String> pcInputLines = p.apply(TextIO.read().from(dataPath));
    PCollection<String> pclLines = pcInputLines.apply(Filter.by((String line) -> !line.isEmpty()));
    PCollection<String> pcInputEmptyLines = pclLines.apply(Filter.by((String line) -> !line.equals("")));
    PCollection<String> pcInputLinkLines = pcInputEmptyLines.apply(Filter.by((String line) -> line.startsWith("[")));

    PCollection<String> pcInputLinks = pcInputLinkLines.apply(
        MapElements.into(TypeDescriptors.strings())
            .via((String linelink) -> linelink.substring(linelink.indexOf("(") + 1, linelink.indexOf(")"))));

    PCollection<KV<String, String>> pckvLinks = pcInputLinks.apply(
        MapElements.into(
            TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
            .via(linelink -> KV.of(dataFile, linelink)));

    return pckvLinks;
  }

}