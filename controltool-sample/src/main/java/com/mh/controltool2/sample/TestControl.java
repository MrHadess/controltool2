package com.mh.controltool2.sample;

import com.mh.controltool2.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test") // first match url
public class TestControl {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Default Request method is full match
     *
     * @param request this object unused annotation will be injection object
     * @param response this object unused annotation will be injection object
     * @return if return data is null,response data will be undo rewrite
     * */
    @RequestMapping("/PrintTime")
    public String printTime(HttpServletRequest request,HttpServletResponse response) {
        return simpleDateFormat.format(new Date());
    }

    /**
     * same to spring,support from 'PathVariable' function...
     * @see PathVariable use url form path param,
     * @see RequestHeader get from request header
     * @see RequestParam get from request param
     *
     * And you can use to next annotation injection servlet bean object
     * @see Autowired
     * @see Bean
     *
     * */
    @RequestMapping(value = "/Show/{text}" ,method = RequestMethod.GET)
    public String showData(@PathVariable("text") String text , @RequestHeader("content-type") String contentType ,@RequestParam("id") Integer id) {
        return String.format("show text == %s,content-type == %s,id == %s",text,contentType,id);
    }

    /**
     * Request method from POST
     * @param list support 'RequestBody' can be read request body,use json text cover to object
     * @return support everything return object,use to response json data
     * */
    @RequestMapping(value = "/acceptdatagroup" ,method = RequestMethod.POST)
    public DataGroup acceptDataGroup(@RequestBody List<Integer> list) {

        // print accept object from request body
        System.out.println(list);

        // return json object
        DataGroup dataGroup = new DataGroup();
        dataGroup.id = "printReqList";
        dataGroup.name = list.toString();
        return dataGroup;
    }

    public static class DataGroup {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
