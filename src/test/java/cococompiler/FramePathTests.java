package cococompiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cococompiler.frames.FramePath;

public class FramePathTests 
{
    @Test
    public void getNameTest()
    {
        FramePath path = FramePath.of("path/of/file");
        assertEquals(path.getFileName(), "file");
    }    
}
