package cz.muni.fi.obs.etl.step.create.facts;

import cz.muni.fi.obs.data.dbo.TempAccount;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO implement reading:
 * <p>
 * how to do this:
 * <p>
 * if there are accounts present in the collection just pop the first element and return it
 * if there are no accounts read the next page store them into the field, and increment the page, then return first account
 * from read page
 * <p>
 * if there are no accounts left just return null, this automatically terminates upon doing that
 */
@Component
@StepScope
public class TempAccountReader implements ItemReader<TempAccount> {

    private List<TempAccount> accountCache;

    // might be int?
    private long currentPage;

    // if this is set to 0 read the actual value of this param, use it to determine if there should no be accounts anymore
    private long totalPages;

    @Override
    public TempAccount read() {
        return null;
    }
}
