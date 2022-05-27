package com.github.cloudgyb.webserver.http;

import org.junit.Assert;
import org.junit.Test;

public class RangeHeaderDecodeTest {

    @Test
    public void testSingleRangeDecode() {
        RangeHeaderDecode rangeHeaderDecode = new RangeHeaderDecode();
        RangeHeader decode = rangeHeaderDecode.decode("bytes=0-123");
        Assert.assertEquals("bytes", decode.getUnit());
        Assert.assertEquals(1, decode.getRangeStartEnds().size());
    }

    @Test
    public void testSingleRangeDecode1() {
        RangeHeaderDecode rangeHeaderDecode = new RangeHeaderDecode();
        RangeHeader decode = rangeHeaderDecode.decode("bytes=0-");
        Assert.assertEquals("bytes", decode.getUnit());
        Assert.assertEquals(1, decode.getRangeStartEnds().size());
    }

    @Test
    public void testMultipartRangeDecode() {
        RangeHeaderDecode rangeHeaderDecode = new RangeHeaderDecode();
        RangeHeader decode = rangeHeaderDecode.decode("bytes=0-123, 124-500");
        Assert.assertEquals("bytes", decode.getUnit());
        Assert.assertEquals(2, decode.getRangeStartEnds().size());
    }

    @Test
    public void testMultipartRangeDecode2() {
        RangeHeaderDecode rangeHeaderDecode = new RangeHeaderDecode();
        RangeHeader decode = rangeHeaderDecode.decode("bytes=0-123,124-500");
        Assert.assertEquals("bytes", decode.getUnit());
        Assert.assertEquals(2, decode.getRangeStartEnds().size());
    }

    @Test
    public void testMultipartRangeDecode3() {
        RangeHeaderDecode rangeHeaderDecode = new RangeHeaderDecode();
        RangeHeader decode = rangeHeaderDecode.decode("bytes=0-123,124-500, 501-1234");
        Assert.assertEquals("bytes", decode.getUnit());
        Assert.assertEquals(3, decode.getRangeStartEnds().size());
    }
}
