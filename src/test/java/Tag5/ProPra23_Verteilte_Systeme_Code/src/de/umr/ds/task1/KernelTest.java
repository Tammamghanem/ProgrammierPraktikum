package Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class KernelTest {

    @Test
    public void testConvolve() {
        double [][] m = {
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1} };
        Kernel kernel = new Kernel(m);

        int [][] img = {
                {1,0,0},
                {1,1,0},
                {1,1,1}
        };
        int [][] result = kernel.convolve(img);
        Assertions.assertEquals(4,result[0][0]);

        Assertions.assertEquals(img.length - m.length + 1, result.length);
        Assertions.assertEquals(img[0].length - m[0].length + 1, result[0].length);



    }
}