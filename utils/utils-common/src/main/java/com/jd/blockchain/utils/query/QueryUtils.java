package com.jd.blockchain.utils.query;

import static com.jd.blockchain.utils.BaseConstant.QUERY_LIST_MAX;

/**
 * @author zhaogw
 * date 2019/2/22 17:00
 */
public class QueryUtils {

    /**
     * confirm the fromIndex and count by 3 factors;
     * @param from fromIndex
     * @param count count
     * @param maxNum maxNum
     * @return
     */
    public static QueryArgs calFromIndexAndCount(int from, int count, int maxNum) {
        if (from < 0 || from >= maxNum) {
            from = 0;
        }

        //must < maxNum;
        if(count > maxNum){
            count = maxNum;
        }
        //must < threshold;
        if(count > QUERY_LIST_MAX){
            count = QUERY_LIST_MAX;
        }
        //if count is empty, get the small;
        if (count == -1) {
            from = 0;
            //count must <=100;
            count = maxNum > QUERY_LIST_MAX ? QUERY_LIST_MAX : maxNum;
        }
        //count is ok, then calculate the plus condition;
        if (from + count >= maxNum) {
            count = maxNum - from;
        }
        //now if count<-1, then deduce: make trouble;so set count=0;
        if(count < -1){
            count = 0;
        }
        return new QueryArgs(from, count);
    }

    /**
     * cal the data by descend;
     * @param from
     * @param count
     * @param maxNum
     * @return
     */
    public static QueryArgs calFromIndexAndCountDescend(int from, int count, int maxNum){
        QueryArgs queryArgs = calFromIndexAndCount(from, count, maxNum);
        //now use descend; first show the latest record;
        queryArgs.setFrom(maxNum - queryArgs.getFrom() - queryArgs.getCount());
        return queryArgs;
    }
}
