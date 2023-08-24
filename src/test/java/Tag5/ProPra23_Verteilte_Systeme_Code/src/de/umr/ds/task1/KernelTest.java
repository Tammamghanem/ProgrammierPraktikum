package Tag5.ProPra23_Verteilte_Systeme_Code.src.de.umr.ds.task1;

import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

class KernelTest {

    @Test
    void testConvolve() {
        double [][] m = {
                {0, 0, 3},
                {0, 2, 0},
                {1, 0, 0} };
        Kernel kernel = new Kernel(m);

        int [][] img = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
        };
        int [][] result = kernel.convolve(img);
        Assertions.assertEquals(4,result[0][2]);

        Assertions.assertEquals(img.length - m.length + 1, result.length);
        Assertions.assertEquals(img[0].length - m[0].length + 1, result[0].length);



    }
}