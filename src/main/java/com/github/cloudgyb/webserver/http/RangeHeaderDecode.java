package com.github.cloudgyb.webserver.http;

import java.util.HashSet;
import java.util.Set;

/**
 * Http请求头Range解码器
 * <ol>合法的Range头有以下几种：
 * <li>Range: &lt;unit&gt;=&lt;rangeStart&gt;-</li>
 * <li>Range: &lt;unit&gt;=&lt;rangeStart&gt;-&lt;rangeEnd&gt;</li>
 * <li>Range: &lt;unit&gt;=&lt;rangeStart&gt;-&lt;rangeEnd&gt;, &lt;rangeStart&gt;-&lt;rangeEnd&gt;, ...</li>
 * </ol>
 * <pre>
 * &lt;unit&gt;:范围所采用的单位，通常是字节（bytes）。
 * &lt;range-start&gt;:一个整数，表示在特定单位下，范围的起始值。
 * &lt;range-end&gt;:一个整数，表示在特定单位下，范围的结束值。这个值是可选的，如果不存在，表示此范围一直延伸到文档结束。
 * </pre>
 * 更多请参考<a href="https://datatracker.ietf.org/doc/html/rfc7233#section-3.1">RFC 7233, section 3.1: Range</a>
 */
public class RangeHeaderDecode {
    private final static char valueSeparator = '=';
    private final static Set<String> supportUnit;

    static {
        supportUnit = new HashSet<>();
        supportUnit.add("bytes");
    }

    /**
     * 将Range请求头值解析成{@link RangeHeader}对象
     *
     * @param rangeHeaderValue Range请求头值
     * @return {@link RangeHeader}对象
     */
    public RangeHeader decode(String rangeHeaderValue) {
        int index = rangeHeaderValue.indexOf(valueSeparator);
        if (index <= 0) {
            return null;
        }
        if (index == rangeHeaderValue.length() - 1)
            return null;
        String unit = rangeHeaderValue.substring(0, index);
        if (!supportUnit.contains(unit)) {
            return null;
        }
        RangeHeader rangeHeader = new RangeHeader();
        rangeHeader.setUnit(unit);
        String byteFlags = rangeHeaderValue.substring(index + 1);
        int commaIndex = byteFlags.indexOf(',');
        if (commaIndex == -1) { //单range值
            Long[] integers = decodeRangeStartEnd(byteFlags);
            rangeHeader.addRangeStartEnd(integers);
        } else { //多range值
            String[] split = byteFlags.split(",");
            for (String s : split) {
                Long[] integers = decodeRangeStartEnd(s);
                if (integers == null)
                    return null;
                rangeHeader.addRangeStartEnd(integers);
            }
        }
        return rangeHeader;
    }

    private Long[] decodeRangeStartEnd(String byteRangeStartEnd) {
        int index = byteRangeStartEnd.indexOf('-');
        if (index <= 0)
            return null;
        try {
            String startStr = byteRangeStartEnd.substring(0, index);
            long start = Long.parseLong(startStr.trim());
            if (start < 0)
                return null;
            Long end = null;
            if (index < byteRangeStartEnd.length() - 1) {
                String endStr = byteRangeStartEnd.substring(index + 1);
                end = Long.parseLong(endStr.trim());
                if (end <= 0)
                    return null;
            }
            return new Long[]{start, end};
        } catch (NumberFormatException ignore) {
        }
        return null;
    }
}
