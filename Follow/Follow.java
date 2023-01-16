import java.io.IOException;
import java.util.*;
        
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
        
public class Follow {
        
 public static class FollowingMap extends Mapper<LongWritable, Text, Text, Text> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);
	while (tokenizer.hasMoreTokens()) {
            String token1 = tokenizer.nextToken();
            String token2 = tokenizer.nextToken();
            context.write(new Text(token1), new Text(token2));
            context.write(new Text(token2), new Text("$"+token1));
        }
    }
 } 
        
 public static class FollowingReduce extends Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context) 
      throws IOException, InterruptedException {
        String sum1 = "following list --> [";
        String sum2 = "follower list --> [";
        int sum1_size = sum1.length();
        int sum2_size = sum2.length();
        for (Text val : values) {
            if (val.toString().startsWith("$")){
                sum2 += val.toString().substring(1);
                sum2 += ",";
            }
            else{
                sum1 += val;
                sum1 += ",";
            }
            
        }
        if (sum1.length() != sum1_size)
            sum1 = sum1.substring(0,sum1.length()-1); //to remove the last ','
        sum1 += "]";
        if (sum2.length() != sum2_size)
            sum2 = sum2.substring(0,sum2.length()-1); //to remove the last ','
        sum2 += "]";
        context.write(key, new Text("\n"+sum1+"\n"+sum2));
    }
 }

        
 public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
        
    Job job = new Job(conf, "follow");
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
        
    job.setMapperClass(FollowingMap.class);
    job.setReducerClass(FollowingReduce.class);
    job.setJarByClass(Follow.class);
        
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
        
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
    job.waitForCompletion(true);

 }
        
}
