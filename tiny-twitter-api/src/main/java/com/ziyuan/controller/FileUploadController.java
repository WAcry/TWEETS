package com.ziyuan.controller;

import com.ziyuan.resource.FileUpload;
import com.ziyuan.utils.DateUtil;
import com.ziyuan.utils.JSONResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileUploadController {
    @Autowired
    private FileUpload fileUpload;

    @PostMapping("upload")
    public JSONResult upload(
            @RequestParam String userId,
            MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {

        String fileSpace = fileUpload.getImageUserFaceLocation();
        String uploadPathPrefix = File.separator + userId;

        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                String fileName = file.getOriginalFilename();

                if (StringUtils.isNotBlank(fileName)) {
                    //   ziyuan.png -> ["ziyuan", "png"]
                    String fileNameArr[] = fileName.split("\\.");

                    String suffix = fileNameArr[fileNameArr.length - 1];

                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg")) {
                        return JSONResult.errorMsg("invalid file type");
                    }

                    // face-{userid}.png
                    String newFileName = "face-" + userId + "." + suffix;

                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            return JSONResult.errorMsg("the file can not be empty");
        }

        String imageServerUrl = fileUpload.getImageServerUrl();

        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        return JSONResult.ok(finalUserFaceUrl);
    }

}
