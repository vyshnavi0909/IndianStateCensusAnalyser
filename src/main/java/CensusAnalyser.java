import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath) throws  CensusAnalyserException {
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            CsvToBeanBuilder<IndianStatesCensusAnalyser> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(IndianStatesCensusAnalyser.class);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<IndianStatesCensusAnalyser> csvToBean = csvToBeanBuilder.build();
            Iterator<IndianStatesCensusAnalyser> censusAnalyserIterator = csvToBean.iterator();
            Iterable<IndianStatesCensusAnalyser> iterable = () -> censusAnalyserIterator;
            int numOfEntries = (int) StreamSupport.stream(iterable.spliterator(),false).count();
            return numOfEntries;
        }catch (IOException e){
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }catch (IllegalStateException e){
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }

    public static void main(String[] args) throws CensusAnalyserException {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int entries = censusAnalyser.loadIndiaCensusData("/home/babbur/IdeaProjects/IndianStatesCensusAnalyser/src/test/resources/IndiaStateCensusData.csv");
        System.out.println(entries);
    }
}
