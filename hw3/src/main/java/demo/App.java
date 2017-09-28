package demo;

import org.apache.commons.cli.*;

import java.util.*;

public class App {

    private static int pick(Random random, double[] cdf) {
        double target = random.nextDouble();
        int mid;
        int left = 0;
        int right = cdf.length - 1;
        while (left + 1 < right) {
            mid = left + (right - left) / 2;
            if (target < cdf[mid]) {
                right = mid;
            } else {
                left = mid;
            }
        }
        int result = left;
        if (target > cdf[left]) {
            result = right;
        }
        //System.out.printf("target: %f, result: %d\n", target, result);
        return result;
    }

    public static void main(String[] args) {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder().longOpt("weights")
                .desc("weights of each elements")
                .type(String.class)
                .required()
                .hasArg()
                .argName("w")
                .build());
        options.addOption(Option.builder().longOpt("samples")
                .desc("times of sampling")
                .type(Long.class)
                .required()
                .hasArg()
                .argName("s")
                .build());
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            String weights = line.getOptionValue("weights");
            Long samples = Long.parseLong(line.getOptionValue("samples"));
            System.out.printf("weights: %s, samples: %s\n", weights, samples);

            // calculate CDF
            String[] strArray = weights.split(",");
            double[] cdf = new double[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                cdf[i] = Double.parseDouble(strArray[i]);
                if (i > 0) {
                    cdf[i] += cdf[i - 1];
                }
            }

            // normalize CDF
            for (int i = 0; i < cdf.length; i++) {
                cdf[i] /= cdf[cdf.length - 1];
            }

            System.out.println("CDF: " + Arrays.toString(cdf));

            Random random = new Random(System.currentTimeMillis());

            // select item
            ArrayList<Integer> result = new ArrayList<Integer>();
            for (int i = 0; i < samples; i++) {
                result.add(pick(random, cdf));
            }

            System.out.println("Result: " + Arrays.toString(result.toArray()));
            HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();
            for (int index: result) {
                if (!counter.containsKey(index)) {
                    counter.put(index, 0);
                }
                counter.put(index, counter.get(index) + 1);
            }

            System.out.println("Probabilities:");
            for (int i = 0; i < cdf.length; i++) {
                double prob = .0;
                if (counter.containsKey(i)) {
                    prob = counter.get(i) * 100.0 / result.size();
                }
                System.out.printf("%d: %2.2f%%\n", i, prob);
            }

        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "ant", options );
        }
    }
}
