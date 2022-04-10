package com.epam.spsa.s3api;

import com.amazonaws.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PhotoBuilder {

    public static File getPngFile() {
        return new File("src/test/resources/photoTest/pngTest.png");
    }

    public static File getTxtFile() {
        return new File("src\\test\\resources\\photoTest\\txtTest.txt");
    }

    public static byte[] getPngBytes() throws IOException {
        BufferedImage image = ImageIO.read(getPngFile());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public static byte[] getTxtBytes() {
        File file = getTxtFile();
        try (FileInputStream fis = new FileInputStream(file)) {
            return IOUtils.toByteArray(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}