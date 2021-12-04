package edu.estu;


import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.*;


public class App 
{


    public static void main( String[] args ) {
        ArrayList<String> allTokenList = new ArrayList<>();


        MyOption myOption = new MyOption();
        CmdLineParser parser = new CmdLineParser(myOption);

        try{
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("");
            parser.printUsage(System.err);
            return;
        }


        int filenumber = myOption.filename.length;


        for (int i = 0 ; i<filenumber ; i++){
            Path path = Paths.get(myOption.filename[i]);
            if (Files.notExists(path)){
                System.out.println("the file"+myOption.filename[i]+"does not exist!");
                return;
            }
            List<String> lines;
            try{
                lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            }catch(IOException ioe){
                ioe.printStackTrace();
                return;
            }

            for(int a = 0; a<lines.size();a++){
                StringTokenizer st = new StringTokenizer(lines.get(a));
                while (st.hasMoreTokens()){
                    String token = st.nextToken();
                    token = token.toLowerCase(Locale.ENGLISH);

                    for(int b = 0 ; b< token.length(); b++){
                        if(!Character.isLetterOrDigit(token.charAt(b))){
                            String notDigitLetter = String.valueOf(token.charAt(b));
                            token = token.replace(notDigitLetter, "");
                            b--;
                        }
                    }

                    if (!(token.length()==0)){
                        allTokenList.add(token);
                    }

                }


            }






    }

        if (myOption.unique){
            allTokenList = new ArrayList<>(new HashSet<>(allTokenList));
        }


        if (myOption.task.equals("NumOfTokens")){
            System.out.println(allTokenList.size());
        }

        if(myOption.task.equals("TermLengthStats")){
            int max = allTokenList.get(0).length();
            int min = allTokenList.get(0).length();
            double average = 0;

            for(String s : allTokenList){
                average+= s.length();
                if (s.length()> max)
                    max = s.length();
                if(s.length() < min)
                    min= s.length();

            }
            System.out.println("Max Token Length in Character: " + max + " Min Token Length: " + min + " Average Token Length: " + average/((double) allTokenList.size()));
        }

        String start = myOption.start;
        if(myOption.task.equals("TermsStartWith")){
            allTokenList = new ArrayList<>(new HashSet<>(allTokenList));

            Collator trCollator = Collator.getInstance(Locale.forLanguageTag("TR-tr"));
            trCollator.setStrength(Collator.PRIMARY);
            Collections.sort(allTokenList, trCollator);

            if (myOption.reverse)
                Collections.reverse(allTokenList);


            for (String token : allTokenList){
                if (token.startsWith(start)&&(myOption.topN!=0)){
                    System.out.println(token);
                    myOption.topN--;
                }
            }
        }


        if(myOption.task.equals("FrequentTerms")){
            ArrayList<FrequentList> dataList = new ArrayList<>();    /** This array list hold the all data */
            ArrayList<String> uniqueList = new ArrayList<>(new HashSet<>(allTokenList));         /** This arraylist hold the unique sample of tokens */
            Collator trCollator = Collator.getInstance(Locale.forLanguageTag("TR-tr"));
            trCollator.setStrength(Collator.PRIMARY);
            Collections.sort(allTokenList, trCollator);             /** I sort the two list because of when two or more tokens have same frequent , then sort by the alpebetic */
            Collections.sort(uniqueList,trCollator);


            if (!myOption.reverse){                 /** This is is reverse the unique list when reverse is false because  of the my sort algorithm that is bottom */
                Collections.reverse(uniqueList);
            }

            /**
             * *This algorithm firstly count the frequent of the tokens then every time of the counting one MyList object creating and adding the
             * Arraylist named database when adding new data compare with all data was adding from start to end if the new data frequent is bigger
             * than it new data adding that index.
             */

            for (String uToken : uniqueList){
                int counter = 0 ;
                for (String token : allTokenList){
                    if (uToken.equals(token))
                        counter++;
                }

                FrequentList<String,Integer> newData = new FrequentList<>(uToken,counter);
                if (dataList.size()==0){
                    dataList.add(newData);
                }
                else {
                    boolean swapStat = false;
                    for (int i = 0 ; i< dataList.size() ; i++){
                        if (((int)(newData.getFrequent()))>=((int)(dataList.get(i).getFrequent()))){
                            dataList.add(i,newData);
                            i= dataList.size();
                            swapStat = true;
                        }
                    }
                    if (!swapStat)
                        dataList.add(newData);
                }



            }

            if (myOption.reverse){
                Collections.reverse(dataList);
            }

            for (int i = 0 ; i< myOption.topN;i++)
                dataList.get(i).print();

        }




    }

}
