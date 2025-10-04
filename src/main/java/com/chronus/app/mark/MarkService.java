package com.chronus.app.mark;

import com.chronus.app.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarkService {

    public HttpResponse<String> addNewMark(Mark markId) {
        return new HttpResponse<String>(201, "Mark added with success!", null);
    }

}
