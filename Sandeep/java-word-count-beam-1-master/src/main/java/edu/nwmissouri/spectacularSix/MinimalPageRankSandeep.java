
package edu.nwmissouri.spectacularSix;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

//import org.apache.beam.model.pipeline.v1.RunnerApi.PCollection;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.TypeDescriptors;


public class MinimalPageRankSandeep {



  static class Job1Finalizer extends DoFn<KV<String, Iterable<String>>, KV<String, SandeepRankedPage>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<String>> element,
        OutputReceiver<KV<String, SandeepRankedPage>> receiver) {
      Integer contributorVotes = 0;
      if (element.getValue() instanceof Collection) {
        contributorVotes = ((Collection<String>) element.getValue()).size();
      }
      ArrayList<SandeepVotingPage> voters = new ArrayList<SandeepVotingPage>();
      for (String voterName : element.getValue()) {
        if (!voterName.isEmpty()) {
          voters.add(new SandeepVotingPage(voterName, contributorVotes));
        }
      }
      receiver.output(KV.of(element.getKey(), new SandeepRankedPage(element.getKey(), voters)));
    }
  }

  static class Job2Mapper extends DoFn<KV<String,SandeepRankedPage>, KV<String, SandeepRankedPage>>{
      @ProcessElement
      public void processElement(@Element KV<String, SandeepRankedPage> element,
      OutputReceiver<KV<String,SandeepRankedPage>> receiver){
        Integer votes = 0;
        ArrayList<SandeepVotingPage> voters = element.getValue().getVoters();
        if(voters instanceof Collection){
           votes = ((Collection<SandeepVotingPage>)voters).size();
        }
        for(SandeepVotingPage vp: voters){
          String pageName = vp.getName();
          Double pageRank = vp.getRank();
          String contributingPageName = element.getKey();
          Double contributingPageRank = element.getValue().getRank();
          SandeepVotingPage contributor = new SandeepVotingPage(contributingPageName, contributingPageRank, votes);
          ArrayList<SandeepVotingPage> arr = new ArrayList<SandeepVotingPage>();
          arr.add(contributor);
          receiver.output(KV.of(vp.getName(), new SandeepRankedPage(pageName,pageRank,arr)));
        }
      }
  }



  static class Job2Updater extends DoFn<KV<String, Iterable<SandeepRankedPage>>, KV<String, SandeepRankedPage>> {
    @ProcessElement
    public void processElement(@Element KV<String, Iterable<SandeepRankedPage>> element,
        OutputReceiver<KV<String, SandeepRankedPage>> receiver) {
    String page = element.getKey();
    Iterable<SandeepRankedPage> rankedPages = element.getValue();
    Double dampingFactor = 0.85;
    Double updatedRank = (1-dampingFactor);
    ArrayList<SandeepVotingPage> newVoters = new ArrayList<SandeepVotingPage>();
    for(SandeepRankedPage pg : rankedPages){
      if(pg != null){
        for(SandeepVotingPage vPage : pg.getVoters()){
          newVoters.add(vPage);
          updatedRank += (dampingFactor) * vPage.getRank() / (double)vPage.getVotes();
        }
      }
    }
    receiver.output(KV.of(page, new SandeepRankedPage(page, updatedRank, newVoters)));
    }

  }

  static class Job3Finder extends DoFn<KV<String, SandeepRankedPage>, KV<String, Double>> {
		@ProcessElement
		public void processElement(@Element KV<String, SandeepRankedPage> element,
				OutputReceiver<KV<String, Double>> receiver) {
			String currentPage = element.getKey();
			Double currentPageRank = element.getValue().getRank();

			receiver.output(KV.of(currentPage, currentPageRank));
		}
	}

	public static class Job3Final implements Comparator<KV<String, Double>>, Serializable {
		@Override
		public int compare(KV<String, Double> o1, KV<String, Double> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	}


  public static void main(String[] args) {
    deleteFiles();

    PipelineOptions options = PipelineOptionsFactory.create();

    Pipeline p = Pipeline.create(options);



    String dataFolder = "web04";
    String dataFile = "go.md";
    String dataPath = dataFolder + "/" + dataFile;
    //p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV1 = sandeepFirstMapJob(p, dataFile, dataPath);
            
      
     dataFile = "java.md";
     dataPath = dataFolder + "/" + dataFile;
    //p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV2 = sandeepFirstMapJob(p, dataFile, dataPath);
    

     dataFile = "python.md";
     dataPath = dataFolder + "/" + dataFile;
    //p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV3 = sandeepFirstMapJob(p, dataFile, dataPath);

     dataFile = "README.md";
     dataPath = dataFolder + "/" + dataFile;
    //p.apply(TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"))

    PCollection<KV<String, String>> pckvLinksKV4 = sandeepFirstMapJob(p, dataFile, dataPath);

    PCollectionList<KV<String, String>> myLst = PCollectionList.of(pckvLinksKV1).and(pckvLinksKV2).and(pckvLinksKV3).and(pckvLinksKV4);

    PCollection<KV<String, String>> myMergeLst = myLst.apply(Flatten.<KV<String,String>>pCollections());

    PCollection<KV<String, Iterable<String>>> MakinggGoupByLst = myMergeLst.apply(GroupByKey.<String, String>create());

    PCollection<KV<String, SandeepRankedPage>> jobOne = MakinggGoupByLst.apply(ParDo.of(new Job1Finalizer()));


    PCollection<KV<String, SandeepRankedPage>> updatedOut = null;
PCollection<KV<String, SandeepRankedPage>> mappedKVs = null;

int iterations =50;
for (int i =0; i<iterations; i++){
  if(i==0){
    mappedKVs = jobOne
      .apply(ParDo.of(new Job2Mapper()));
  }else{
    mappedKVs = updatedOut
      .apply(ParDo.of(new Job2Mapper()));
  }      
  PCollection<KV<String, Iterable<SandeepRankedPage>>> reducedKVs = mappedKVs
    .apply(GroupByKey.<String, SandeepRankedPage>create());
  updatedOut = reducedKVs.apply(ParDo.of(new Job2Updater()));
}

    PCollection<String> pckvLinks =  updatedOut.apply(
      MapElements.into(  
        TypeDescriptors.strings())
          .via((myMergeLstout) -> myMergeLstout.toString())
        );

  
    pckvLinks.apply(TextIO.write().to("Sandeepwordcounts"));
       

    p.run().waitUntilFinish();
  }

  private static PCollection<KV<String, String>> sandeepFirstMapJob(Pipeline p, String dataFile, String dataPath) {
    PCollection<String> pcInputLines =  p.apply(TextIO.read().from(dataPath));
    PCollection<String> pclLines  =pcInputLines.apply(Filter.by((String line) -> !line.isEmpty()));
    PCollection<String> pcInputEmptyLines=pclLines.apply(Filter.by((String line) -> !line.equals("")));
    PCollection<String> pcInputLinkLines=pcInputEmptyLines.apply(Filter.by((String line) -> line.startsWith("[")));
    
    PCollection<String> pcInputLinks=pcInputLinkLines.apply(
            MapElements.into(TypeDescriptors.strings())
                .via((String linelink) -> linelink.substring(linelink.indexOf("(")+1,linelink.indexOf(")")) ));

                PCollection<KV<String, String>> pckvLinks=pcInputLinks.apply(
                  MapElements.into(  
                    TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
                      .via (linelink ->  KV.of(dataFile , linelink) ));
      
                    
    return pckvLinks;
  }

  public static  void deleteFiles(){
    final File file = new File("./");
    for (File f : file.listFiles()){
      if(f.getName().startsWith("Sandeep")){
        f.delete();
      }
    }
  }




}
