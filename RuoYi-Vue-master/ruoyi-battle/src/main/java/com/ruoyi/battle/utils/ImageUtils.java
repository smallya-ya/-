package com.ruoyi.battle.utils;

import com.google.common.collect.ListMultimap;
import com.ruoyi.battle.shootingvest.domian.Point; // 确保导入自定义Point类
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hongjiasen
 */
public class ImageUtils {

    public static String pressBatchBackGroudText(Map<Integer, Point> pressTextMap, String imgBase64 // 修改为自定义Point类
            , String fontName, int fontStyle, int fontSize, Color fontColor, Map<Integer, Color> backGroundColorMap) {
        try {
            byte[] bytes = Base64.getDecoder().decode(imgBase64.trim());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Image src = ImageIO.read(inputStream);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);

            for (Map.Entry<Integer, Point> entry : pressTextMap.entrySet()) { // 修改为自定义Point类
                g.setFont(new Font(fontName, fontStyle, fontSize));
                int fontHight = g.getFontMetrics().getHeight();
                int fontWidth = g.getFontMetrics().stringWidth(entry.getKey().toString());
                g.setColor(backGroundColorMap.get(entry.getKey()));
                g.fillRect((int) entry.getValue().getLng(), (int) entry.getValue().getLat() + 1 - fontHight, fontWidth, fontHight);
                g.setColor(fontColor);
                g.drawString(String.valueOf(entry.getKey()), (int) entry.getValue().getLng(), (int) entry.getValue().getLat());
            }
            g.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String pressBatchBackGroudText(Map<Integer, Point> pressTextMap, ListMultimap<Integer, Point> pointMap // 修改为自定义Point类
            , String imgBase64, String fontName, int fontStyle, int fontSize, Color fontColor
            , Map<Integer, Color> backGroundColorMap) {
        try {
            byte[] bytes = Base64.getDecoder().decode(imgBase64.trim());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Image src = ImageIO.read(inputStream);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);

            for (Map.Entry<Integer, Point> entry : pressTextMap.entrySet()) { // 修改为自定义Point类
                g.setFont(new Font(fontName, fontStyle, fontSize));
                int fontHight = g.getFontMetrics().getHeight();
                int fontWidth = g.getFontMetrics().stringWidth(entry.getKey().toString());
                g.setColor(backGroundColorMap.get(entry.getKey()));

                java.util.List<Point> points = pointMap.get(entry.getKey());
                for (int i = 0; i < points.size() - 1; i++) {
                    g.drawLine((int) points.get(i).getLng(), (int) points.get(i).getLat(),
                            (int) points.get(i + 1).getLng(), (int) points.get(i + 1).getLat());
                }
                g.drawLine((int) points.get(points.size() - 1).getLng(), (int) points.get(points.size() - 1).getLat(),
                        (int) entry.getValue().getLng(), (int) entry.getValue().getLat());
                g.fillRect((int) entry.getValue().getLng(), (int) entry.getValue().getLat() + 1 - fontHight, fontWidth,
                        fontHight);
                g.setColor(fontColor);
                g.drawString(String.valueOf(entry.getKey()), (int) entry.getValue().getLng(),
                        (int) entry.getValue().getLat());
            }
            g.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isRgbOrCmyk(InputStream is) throws IOException {
        // true是Rgb否则是Cmyk
        boolean isRgb = true;
        // 创建输入流
        ImageInputStream input = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
        if (readers == null || !readers.hasNext()) {
            throw new RuntimeException("No ImageReaders found");
        }
        ImageReader reader = (ImageReader) readers.next();
        reader.setInput(input);
        try {
            // 尝试读取图片 (包括颜色的转换).
            reader.read(0);
        } catch (IIOException e) {
            // 读取Raster (没有颜色的转换).
            reader.readRaster(0, null);
            isRgb = false;
        }
        return isRgb;
    }

    public static BufferedImage readImage(InputStream is) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (readers == null || !readers.hasNext()) {
            return null;
        }

        ImageReader reader = (ImageReader) readers.next();
        reader.setInput(iis);

        BufferedImage image;
        try {
            // 尝试读取图片 (包括颜色的转换).
            image = reader.read(0);

        } catch (IIOException e) {
            // 读取Raster (没有颜色的转换).
            Raster raster = reader.readRaster(0, null);
            image = createjpeg4(raster);
        }

        return image;
    }

    private static BufferedImage createjpeg4(Raster raster) {
        int w = raster.getWidth();
        int h = raster.getHeight();
        byte[] rgb = new byte[w * h * 3];

        // 彩色空间转换
        float[] yy = raster.getSamples(0, 0, w, h, 0, (float[]) null);
        float[] cbs = raster.getSamples(0, 0, w, h, 1, (float[]) null);
        float[] crs = raster.getSamples(0, 0, w, h, 2, (float[]) null);
        float[] kk = raster.getSamples(0, 0, w, h, 3, (float[]) null);

        int bases = 3;
        for (int i = 0, imax = yy.length, base = 0; i < imax; i++, base += bases) {
            float k = 220 - kk[i], y = 255 - yy[i], cb = 255 - cbs[i], cr = 255 - crs[i];

            double val = y + 1.402 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y + 1.772 * (cb - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
        }

        raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3,
                new int[] { 0, 1, 2 }, null);

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
    }

}
