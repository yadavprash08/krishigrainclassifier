package com.prashant.java.krishi.classifier.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.SimpleBufferedImageFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@RestController
@Slf4j
public class ImageController {

    private static final File DIRECTORY = new File("/tmp/krishiml");
    private static final Map<String, Object> IMAGE_OPEN_PARAMS = new HashMap<>();

    static {
        IMAGE_OPEN_PARAMS.put(ImagingConstants.BUFFERED_IMAGE_FACTORY, new SimpleBufferedImageFactory());
    }

    @RequestMapping(value = "wheat/process/image", method = RequestMethod.GET)
    @ResponseBody
    public byte[] processImage(WebRequest request, HttpServletResponse response)
        throws Exception {
        assertThat(request).isNotNull();
        String filePath = request.getParameter("reqestedFile");
        File inpImageFile = new File(filePath);
        assertThat(inpImageFile.exists()).isEqualTo(true);
        final StringBuilder sb = new StringBuilder();
        log.info("Reading image {}", filePath);

        final BufferedImage bufferedImage = Imaging.getBufferedImage(inpImageFile, IMAGE_OPEN_PARAMS);
        return sb.toString().getBytes();
    }
}
