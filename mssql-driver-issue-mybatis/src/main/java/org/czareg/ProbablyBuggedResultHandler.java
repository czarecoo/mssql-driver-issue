package org.czareg;

import java.util.ArrayList;
import java.util.List;

public class ProbablyBuggedResultHandler implements SimpleResultHandler<Columns> {

    List<Columns> finalResults = new ArrayList<>();
    List<Columns> partialResults = new ArrayList<>();
    boolean more = true;
    int batchSize = 265 * 4;

    @Override
    public boolean handleResult(Columns result) {
        if (!more) {
            return false;
        }

        if(partialResults.size()>= batchSize){
            preloadFilterAndAddToFinal();
        }

        partialResults.add(result);
        if(partialResults.size()>10){
            this.more = false;
            return more;
        }
        System.out.println(result);
        return more;
    }

    public void done(){
        if(partialResults.size() >=0){
            preloadFilterAndAddToFinal();
        }
    }

    private void preloadFilterAndAddToFinal() {
        // preloading caches
        for (Columns partialResult : partialResults) {
            // filtering
            finalResults.add(partialResult);
        }
        partialResults.clear();
    }
}
