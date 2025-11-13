package com.example.SocialNetwork.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

public class PageableUtils {
    public static Pageable createPageable(int page, int size, String...sortBy){
        Sort sort = Sort.by(sortBy);
        if (Arrays.asList(sortBy).contains("time"))
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        return PageRequest.of(page, size, sort);
    }
}
