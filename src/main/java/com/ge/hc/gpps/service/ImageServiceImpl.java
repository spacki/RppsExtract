package com.ge.hc.gpps.service;

import com.ge.hc.gpps.domain.Image;
import com.ge.hc.gpps.repository.ImageDao;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/26/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageServiceImpl implements ImageService {

    private ImageDao imageDao;

    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public Image findImage(String sop) {
        return this.imageDao.findImage(sop);
    }
}
