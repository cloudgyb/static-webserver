package com.github.cloudgyb.webserver.util;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilsTest {
    @Test
    public void test() {
        String fsdfdsf = FileUtils.getSuffix("fsdfdsf");
        Assert.assertEquals("", fsdfdsf);
    }

    @Test
    public void test1() {
        String fsdfdsf = FileUtils.getSuffix("fsdfdsf.");
        Assert.assertEquals(".", fsdfdsf);
    }

    @Test
    public void test2() {
        String fsdfdsf = FileUtils.getSuffix("fsdfdsf.txt");
        Assert.assertEquals(".txt", fsdfdsf);
    }
}
