package com.app.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ApiResponse<T> {

    private List<T> data;
    private PageInfoResponse pageInfo;
}
