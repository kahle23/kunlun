package com.apyhs.artoria.time;

import java.text.ParseException;
import java.util.Date;

public interface DateParser {

    Date parse(String dateString, String pattern) throws ParseException;

}
