import play.filters.csrf.CSRFFilter;
import play.http.DefaultHttpFilters;
import play.filters.gzip.GzipFilter;
import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {
    @Inject
    public Filters(GzipFilter gzip, LoggingFilter logging, CSRFFilter csrf) {
        super(gzip, logging, csrf);
    }
}