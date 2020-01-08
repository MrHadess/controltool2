package com.mh.controltool2.sample;

import com.mh.controltool2.annotation.RequestMapping;
import com.mh.controltool2.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/helloworld")
public class TestControl {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/PrintTime")
    public String printTime() {
        return simpleDateFormat.format(new Date());
    }


}
