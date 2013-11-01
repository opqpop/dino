package com.lolRiver.river.util;

import com.lolRiver.config.ConfigMap;
import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
/**
 * @author mxia (mxia@groupon.com)
 *         10/12/13
 */

public class ImageDownloader {
    // not used because i found better way of downloading skin images, but shows how to use yml file.
    public static void downloadSkinImages() {
        String SKINS_FILE = new ConfigMap().getString("download.skins_file");
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(SKINS_FILE);
        List<String> list = (List<String>)new Yaml().load(inputStream);
        ImageDownloader.downloadImage(list, "src/main/webapp/static/images/skins/");
    }

    public static void downloadImage(List<String> urls, String outputPath) {
        for (String url : urls) {
            downloadImage(url, outputPath);
        }
    }

    public static void downloadImage(String url, String outputPath) {
        try {
            URL realUrl = new URL(url);
            BufferedImage image = ImageIO.read(realUrl);

            String imageName = url.substring(url.lastIndexOf('/') + 1);
            File file = new File(outputPath + imageName);
            file.createNewFile();

            if (url.endsWith(".png")) {
                ImageIO.write(image, "png", file);
            } else if (url.endsWith(".jpg")) {
                ImageIO.write(image, "jpg", file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
