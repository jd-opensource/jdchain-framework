package com.jd.blockchain.sdk.converters;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.DataType;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.ledger.EventInfo;
import com.jd.blockchain.ledger.TypedValue;
import com.jd.blockchain.utils.http.HttpServiceContext;
import com.jd.blockchain.utils.http.ResponseConverter;
import com.jd.blockchain.utils.http.agent.ServiceRequest;
import com.jd.blockchain.utils.http.converters.JsonResponseConverter;
import com.jd.blockchain.utils.serialize.json.JSONSerializeUtils;
import com.jd.blockchain.utils.web.client.WebServiceException;
import com.jd.blockchain.utils.web.model.WebResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 事件查询结果转换
 */
public class EventResponseConverter implements ResponseConverter {

    private JsonResponseConverter jsonConverter = new JsonResponseConverter(WebResponse.class);

    @Override
    public Object getResponse(ServiceRequest request, InputStream responseStream, HttpServiceContext serviceContext) throws Exception {
        WebResponse response = (WebResponse) jsonConverter.getResponse(request, responseStream, null);
        if (response == null) {
            return null;
        }
        if (response.getError() != null) {
            throw new WebServiceException(response.getError().getErrorCode(), response.getError().getErrorMessage());
        }
        if (response.getData() == null) {
            return null;
        }

        if (response.getData() instanceof JSONArray) {
            List<Event> events = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) response.getData();
            for (Object obj : jsonArray) {
                events.add(parseEvent((JSONObject) obj));
            }
            return events.toArray(new EventInfo[events.size()]);
        } else {
            return parseEvent((JSONObject) response.getData());
        }
    }

    /**
     *
     * {
     *   "sequence": 3,
     *   "transactionSource": {
     *     "value": "j5qcFcMGfVwd6Bp1SmexSD4gPpoZM1iuCfTv95X3c4dib8"
     *   },
     *   "blockHeight": 1,
     *   "contractSource": "",
     *   "eventAccount": {
     *     "value": "LdeNxNjGYDr5qETaPvoPdJBSBQ2QCMaw5tRFp"
     *   },
     *   "name": "event1",
     *   "content": {
     *     "nil": false,
     *     "bytes": {
     *       "value": "11111114"
     *     },
     *     "type": "INT64",
     *     "value": 3
     *   }
     * }
     *
     * @param object
     * @return
     */
    private Event parseEvent(JSONObject object) {
        EventInfo info = JSONSerializeUtils.deserializeAs(object, EventInfo.class);
        BytesValue tv = info.getContent();
        JSONObject content = object.getJSONObject("content");
        DataType dt = JSONSerializeUtils.deserializeAs(content.getString("type"), DataType.class);
        String value = content.getString("value");
        switch (dt) {
            case TEXT:
                tv = TypedValue.fromText(value);
                break;
            case INT64:
                tv = TypedValue.fromInt64(Long.valueOf(value));
                break;
        }
        info.setContent(tv);
        return info;
    }

}
