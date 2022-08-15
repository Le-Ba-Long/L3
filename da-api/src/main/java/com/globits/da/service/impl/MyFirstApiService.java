package com.globits.da.service.impl;

import com.globits.da.dto.PostDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class MyFirstApiService {
    public String getMyFirstApi() {
        return "My First Api Service";
    }

    public PostDto getPostDto(@RequestBody PostDto postDto) {
        return postDto;
    }
}
