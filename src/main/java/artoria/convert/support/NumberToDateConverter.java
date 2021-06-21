package artoria.convert.support;

import artoria.convert.ConversionProvider;
import artoria.time.DateUtils;
import artoria.util.Assert;

import java.util.Date;

import static artoria.common.Constants.ONE_THOUSAND;

public class NumberToDateConverter extends AbstractClassConverter {
    private Boolean unixTimestamp = false;

    public NumberToDateConverter(ConversionProvider conversionProvider) {

        super(conversionProvider, Number.class, Date.class);
    }

    public Boolean getUnixTimestamp() {

        return unixTimestamp;
    }

    public void setUnixTimestamp(Boolean unixTimestamp) {
        Assert.notNull(unixTimestamp, "Parameter \"unixTimestamp\" must not null. ");
        this.unixTimestamp = unixTimestamp;
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {
        Number number = (Number) source;
        long lg = number.longValue();
        lg = unixTimestamp ? lg * ONE_THOUSAND : lg;
        Date date = DateUtils.parse(lg);
        // Maybe target is sql date or timestamp
        return getConversionProvider().convert(date, targetClass);
    }

}
