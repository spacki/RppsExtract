package com.ge.hc.gpps.repository;

import com.ge.hc.gpps.domain.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/26/12
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageDao {

    public Image findImage(String sop);


}
