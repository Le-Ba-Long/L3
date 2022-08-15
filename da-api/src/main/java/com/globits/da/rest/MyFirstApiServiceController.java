package com.globits.da.rest;

import com.globits.da.dto.PostDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.file.ExcelGenerator;
import com.globits.da.service.impl.MyFirstApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/my-first-api")
public class MyFirstApiServiceController {
    private static final Logger logger = LoggerFactory.getLogger(MyFirstApiServiceController.class);
    @Autowired
    MyFirstApiService myFirstApiService;

    @PostMapping("/post-dto-param")//Ex 6.1
    public ResponseEntity<ResponseRequest> postMyFirstAPI(@RequestParam("code") String code, @RequestParam("name") String name, @RequestParam("age") Integer age) {
       return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseRequest(200, "Success", new PostDto(code, name, age)));

    }

    @PostMapping("/post-dto-path-variable/{code}/{name}/{age}")//Ex 10
    public ResponseEntity<ResponseRequest> postMyFirstAPI(@PathVariable String code, @PathVariable String name, @PathVariable int age) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseRequest(200, "Success", new PostDto(code, name, age)));
    }

    @PostMapping("/post-dto-no-body")//Ex 11
    public ResponseEntity<ResponseRequest> postMyFirstAPI(PostDto postDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseRequest(200, "Success", postDTO));
    }

    @GetMapping("/call-my-first-api-service")
    public String callMyFirstApi() {
        return myFirstApiService.getMyFirstApi();
    }

    @PostMapping("/post-dto")
    public ResponseEntity<ResponseRequest> postDto(@RequestBody PostDto postDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseRequest(200, "Query Success",
                        myFirstApiService.getPostDto(postDto)));
    }

    @PostMapping("/file")
    public ResponseEntity<?> postMyFirstApiWithMultipartFile(@RequestParam("file") MultipartFile[] files) {
        String result = null;
        try {
            for (MultipartFile file : files) {
                result = new String(file.getBytes());
                System.out.println(result);
                System.out.println(file.getInputStream());

            }

        } catch (IOException e) {
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/read-file-excel")
    public ResponseEntity<?> readFileExcel(@RequestParam("file") MultipartFile files) throws IOException {
        Object obj = ExcelGenerator.readFileExcel(files);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
}
