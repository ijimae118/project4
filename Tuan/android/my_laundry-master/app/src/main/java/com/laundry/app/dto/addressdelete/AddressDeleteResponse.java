package com.laundry.app.dto.addressdelete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

public class AddressDeleteResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public Object data;
}
