package com.eavy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.apache.tika.Tika;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class DataController {

    private final ResourceLoader resourceLoader;
    private final Storage storage;
    private final ObjectMapper objectMapper;

    public DataController(ResourceLoader resourceLoader, Storage storage, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.storage = storage;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/data")
    public ResponseEntity<String> getData() {
        Page<Blob> list = storage.list("breath-of-ai");
        JSONArray jsonArray = new JSONArray();
        list.iterateAll().forEach(b -> {
            JSONObject json = new JSONObject();
            json.put("filename", b.getName());
            json.put("url", b.signUrl(15l, TimeUnit.MINUTES));
            jsonArray.add(json);
        });
        return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> fileUpload(@RequestParam("files") MultipartFile[] files) throws IOException {
        Tika tika = new Tika();
//        Storage storage = StorageOptions.newBuilder().setCredentials(ServiceAccountCredentials.fromStream(resourceLoader.getResource("classpath:breath-of-ai-282d25539b0d.json").getInputStream())).build().getService();
        for(MultipartFile file : files) {
            String mimeType = tika.detect(file.getOriginalFilename());
            if(!mimeType.startsWith("image"))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);

            BlobId blobId = BlobId.of("breath-of-ai", file.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, file.getBytes());
        }
        return new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);
    }

    @PostMapping("/upload-local")
    public ResponseEntity<String> fileUploadLocal(@RequestParam("files") MultipartFile[] files) throws IOException {
        Tika tika = new Tika();
        for(MultipartFile file : files) {
            String mimeType = tika.detect(file.getOriginalFilename());
            if(!mimeType.startsWith("image"))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);

            byte[] bytes = file.getBytes();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream("./images/" + file.getOriginalFilename()));

            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
        return new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);
    }

}
