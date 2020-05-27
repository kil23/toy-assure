package com.channel.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@RestController
public class GetChannelFileRestController {

    private static final Logger logger = Logger.getLogger(GetChannelFileRestController.class);
    private static final String PDF_PATH = "src/main/resources/com/channel/";

    @Autowired private ServletContext context;

    @RequestMapping(value = "/api/channel/download/pdf/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable("fileName") String fileName) {
        logger.info("Calling Download:- " + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "filename=" + fileName+".pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<byte[]> response = null;
        File file = new File(String.valueOf(Paths.get(PDF_PATH+fileName+".pdf")));
        if(file.exists() && file.isFile()) {
            try {
                byte[] inFileBytes = Files.readAllBytes(Paths.get(PDF_PATH+fileName+".pdf"));
                logger.info(inFileBytes.length);
                headers.setContentLength(inFileBytes.length);
                response = new ResponseEntity<>(inFileBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        return null;
    }
}
