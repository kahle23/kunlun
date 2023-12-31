package artoria.convert.support;

import artoria.convert.ConversionService;
import artoria.time.DateUtils;
import artoria.util.Assert;

import java.util.Date;

import static artoria.common.Constants.ONE_THOUSAND;

public class NumberToDateConverter extends AbstractClassConverter {
    private Boolean unixTimestamp = false;

    public NumberToDateConverter() {

        super(Number.class, Date.class);
    }

    public NumberToDateConverter(ConversionService conversionService) {

        super(conversionService, Number.class, Date.class);
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
        return getConversionService().convert(date, targetClass);
    }

}
