package org.czareg.prod;

import org.czareg.entity.FilterSortNode;
import org.czareg.entity.Node;

import java.util.ArrayList;
import java.util.List;

public class ProbablyBuggedResultHandler implements SimpleResultHandler<FilterSortNode> {

    //List<FilterSortNode> finalResults = new ArrayList<>();
    List<FilterSortNode> partialResults = new ArrayList<>();
    boolean more = true;
    int batchSize = 265 * 4;

    @Override
    public boolean handleResult(FilterSortNode result) {
        if (!more) {
            return false;
        }

        if(partialResults.size()>= batchSize){
            preloadFilterAndAddToFinal();
        }

        partialResults.add(result);
        return more;
    }

    public void done(){
        if(partialResults.size() >=0){
            preloadFilterAndAddToFinal();
        }
    }

    private void preloadFilterAndAddToFinal() {
        // preloading caches
        for (FilterSortNode partialResult : partialResults) {
            // filtering
            //finalResults.add(partialResult);
        }
        partialResults.clear();
    }
}
