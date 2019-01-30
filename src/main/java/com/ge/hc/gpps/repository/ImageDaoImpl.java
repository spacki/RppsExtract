package com.ge.hc.gpps.repository;

import com.ge.hc.gpps.domain.Image;
import com.ge.hc.gpps.domain.Study;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/26/12
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDaoImpl implements ImageDao  {

    private static Logger logger = Logger.getLogger(ImageDaoImpl.class);
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    private static String FIND = "select image_ckey, sop_instance_uid from image";


    @Override
    public Image findImage(String sop) {
        String sql = FIND +
                " where sop_instance_uid = :sop";
        logger.warn("excute sql: " + sql);
        Map<String, Object> parameters = null;
        parameters = new HashMap<String, Object>();
        parameters.put("sop", sop);
        Image image = namedParameterJdbcTemplate.queryForObject(sql,parameters,getMapper()) ;
        return image;

    }


    private static class ImageRowMapper  implements RowMapper<Image> {

        @Override
        public Image mapRow(ResultSet resultSet, int i) throws SQLException {
            Image image = new Image();
            image.setId(resultSet.getInt("image_ckey"));
            image.setReason("convert hidden to reject");
            image.setSopInstanstanceUid(resultSet.getString("sop_instance_uid"));
            image.setRejectorId(1);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            image.setDate(formatter.format(date));
            return image;
        }
    }

    private RowMapper<Image>getMapper() {
        return  new ImageRowMapper();
    }
}
