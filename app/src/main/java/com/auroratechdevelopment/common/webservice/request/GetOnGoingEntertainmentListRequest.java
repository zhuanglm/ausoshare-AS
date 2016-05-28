package com.auroratechdevelopment.common.webservice.request;

import com.auroratechdevelopment.common.webservice.WebServiceConstants;
import com.auroratechdevelopment.common.webservice.models.UserInfo;
import com.auroratechdevelopment.common.webservice.response.GetOnGoingAdListResponse;
import com.auroratechdevelopment.common.webservice.response.GetOnGoingEntertainmentListResponse;
import com.auroratechdevelopment.common.webservice.util.WebUtils;

/**
 * Created by Edward on 2015-09-03.
 * Updated by Raymond Zhuang May 10 2016
 */
public class GetOnGoingEntertainmentListRequest extends RequestBase<GetOnGoingEntertainmentListResponse> {

    public String email;
    public String deviceID;
    public String tag;  //advert means advertisement; life means entertainment
    public UserInfo data;
    public String keyword;

    public GetOnGoingEntertainmentListRequest( String email, String Token, String deviceId, UserInfo data, String tag,String key) {
        this.email = email;
        this.deviceID = deviceId;
        this.data = data;
        this.token = Token;
        this.tag = tag;
        keyword = key;
    }

    @Override
    public String getUri() {
        return WebUtils.getURI(String.format(WebServiceConstants.advertOngoingList));
    }

    @Override
    public String getRequestMethodType() {

        return "POST";
    }

    @Override
    public GetOnGoingEntertainmentListResponse getResponse() {
        return new GetOnGoingEntertainmentListResponse();
    }

}
