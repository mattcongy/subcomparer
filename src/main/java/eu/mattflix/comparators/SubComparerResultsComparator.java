package eu.mattflix.comparators;

import eu.mattflix.utils.SubComparerResult;

import java.util.Comparator;

public class SubComparerResultsComparator implements Comparator<SubComparerResult> {


    @Override
    public int compare(SubComparerResult o1, SubComparerResult o2) {
        return o1.getMatchRatio()  < o2.getMatchRatio() ?  -1 : o1.getMatchRatio() == o2.getMatchRatio() ? 0 : 1;
    }
}
