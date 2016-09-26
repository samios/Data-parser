import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by sami on 18/08/16.
 */

public class ParserTest {
    @Test
    public void getConf() throws Exception {
        String conf = "/home/sami/IdeaProjects/Integration/src/test/files/test.conf";
        Parser.parse(conf);
        assertEquals(Conf.getArchiveDir(),"/home/sami/IdeaProjects/Integration/success");
        assertEquals(Conf.getFailArchiveDir(),"/home/sami/IdeaProjects/Integration/fail");
        assertEquals(Conf.getDirectory(),"/home/sami/IdeaProjects/Integration");
        assertEquals(Conf.getPreviewFile(),"/home/sami/IdeaProjects/Wizard/RapportRI3[1][01 08 2014].txt");
        assertEquals(Conf.getColNumber(),"18");
        assertEquals(Conf.getLineFormat(),"Test");
        assertEquals(Conf.getStatusCol(),"1");
        assertEquals(Conf.getLineStart(),"1");
        assertEquals(Conf.getHeader(),"No");
        assertEquals(Conf.getHeaderLine(),"null");
        String[] s={"Passed=Passed","Failed=Failed","Error=Error"};
        assertEquals(Conf.getClient(),"Bub");
        assertEquals(Conf.getWell(),"null");
        assertEquals(Conf.getProductData().getSerial(),"5");

        assertEquals(Conf.getProductFamily(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getDate(),"2+3");
        assertEquals(Conf.getProductData().getBenchTest().getDuration(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getStatus(),"0");
        assertEquals(Conf.getProductData().getBenchTest().getPc(),"8");
        assertEquals(Conf.getProductData().getBenchTest().getFailedMessage(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getName(),"181:Présence varistance");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getDate(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getStatus(),"182");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getMax(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getMin(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getUnit(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getValue(),"null");
        assertEquals(Conf.getProductData().getBenchTest().getSteps().get(0).getStepDuration(),"null");
        assertEquals(Conf.getProductData().getParameters().get(0).getName(),"6");
        assertEquals(Conf.getProductData().getParameters().get(0).getDate(),"null");
        assertEquals(Conf.getProductData().getParameters().get(0).getValue(),"7");
        assertEquals(Conf.getSeparator(),";");
        assertEquals(Conf.getSubSeparator(),"null");
        assertEquals(Conf.getRegex(),"null");
        assertEquals(Conf.getStartKey(),"null");
        assertEquals(Conf.getEndKey(),"null");
        assertEquals(Conf.getStepLineTestStatus(),"null");
    }
    @Test
    public void getWrongConf() throws Exception {
        String conf = "/home/sami/IdeaProjects/Integration/src/test/files/test.conf";
        Parser.parse(conf);
    }

}
