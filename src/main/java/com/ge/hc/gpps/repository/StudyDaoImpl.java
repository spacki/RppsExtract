package com.ge.hc.gpps.repository;

import com.ge.hc.gpps.domain.Study;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/20/12
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudyDaoImpl implements StudyDao {

    private static Logger logger = Logger.getLogger(StudyDaoImpl.class);
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static String SELECT = "select examination.exam_ckey as exam_ckey, ris_exam_id, study_instance_uid, rpps_data " +
            " from examination, rp_presentation_state " +
            " where examination.exam_ckey = rp_presentation_state.exam_ckey " ;


    private static String FIND = "select examination.exam_ckey as exam_ckey, ris_exam_id, study_instance_uid, rpps_data " +
            ",patient_identifier.identifier as identifier, patient.pat_name as pat_name, patient.birth_date as birth_date, " +
            "examination.study_dttm as study_dttm ,exam_procedure.procedure_code as procedure_code, exam_procedure.modality_code as modality_code "  +
            " from examination, rp_presentation_state, patient, exam_procedure, patient_identifier " +
            " where examination.exam_ckey = rp_presentation_state.exam_ckey " +
            " and patient.pat_ckey = examination.pat_ckey " +
            " and examination.procedure_ckey = exam_procedure.procedure_ckey " +
            " and patient.pat_ckey = patient_identifier.pat_ckey "; //" and patient_identifier.authority_ckey = 2";


    private static String SELECTKEYS = "select examination.exam_ckey as exam_ckey " +
            " from examination, rp_presentation_state " +
            " where examination.exam_ckey = rp_presentation_state.exam_ckey ";
           // " and examination.exam_ckey < 100";


    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Integer> getKeys(String updateTS) {

        String sql = SELECTKEYS +
               // " and examination.exam_ckey > :key";
               //" and rp_presentation_state.last_update_dttm> :key";
               " and convert(char(10), last_update_dttm, 102) > :key";
        logger.warn("execute sql: " + sql);
        Map<String, Object> parameters = null;
        parameters = new HashMap<String, Object>();
        parameters.put("key", updateTS);
        List<Map<String, Object>> rows =  namedParameterJdbcTemplate.queryForList(sql,parameters);
        List<Integer> keys = new ArrayList<Integer>();
        for (Map<String, Object> row : rows) {
            Study study = new Study();
            Integer key =  (Integer) row.get("exam_ckey");
            keys.add(key);
        }
        return keys;


    }

    @Override
    public Study getStudy(int id, int authorityCkey) {
        String sql = FIND +
                " and patient_identifier.authority_ckey = :auth_key " +
                " and examination.exam_ckey = :key";
        logger.warn("excute sql: " + sql);
        Map<String, Object> parameters = null;
        parameters = new HashMap<String, Object>();
        parameters.put("auth_key", authorityCkey);
        parameters.put("key", id);
        Study study = namedParameterJdbcTemplate.queryForObject(sql,parameters,getMapper()) ;
        return study;
    }

    @Override
    public List<Study> getStudies() {
        String sql = SELECT;
        logger.debug("query for  Studies: \n" + sql.trim().replaceAll(" +", " "));
        Map<String, Object> parameters = null;
        parameters = new HashMap<String, Object>();
        List<Study> studies = new ArrayList<Study>();
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(sql, parameters);
        for (Map<String, Object> row : rows) {
            Study study = new Study();
            study.setId((Integer) row.get("exam_ckey"));
            study.setStudyInstanceUid((String) row.get("study_instance_uid"));
            study.setAccessionNumber((String) row.get("ris_exam_id"));
            study.setRppsData((byte[]) row.get("rpps_data"));
            study.setPatName((String) row.get("pat_name"));
            study.setBirthDate((String) row.get("birth_date"));
            study.setModality((String) row.get("modality_code"));
            study.setProcedurerCode((String) row.get("procedure_code"));
            study.setStudyDate((String) row.get("study_dttm"));
            study.setPatId((String) row.get("identifier"));
            logger.debug("adding study: " + study.getStudyInstanceUid());
            studies.add(study);
        }
        return studies;
    }

    private static class StudyRowMapper  implements RowMapper<Study> {

        @Override
        public Study mapRow(ResultSet resultSet, int i) throws SQLException {
            Study study = new Study();
            study.setId(resultSet.getInt("exam_ckey"));
            study.setAccessionNumber(resultSet.getString("ris_exam_id"));
            study.setStudyInstanceUid(resultSet.getString("study_instance_uid"));
            study.setRppsData(resultSet.getBytes("rpps_data"));
            study.setPatId(resultSet.getString("identifier"));
            study.setPatName(resultSet.getString("pat_name"));
            study.setBirthDate(resultSet.getString("birth_date"));
            study.setProcedurerCode(resultSet.getString("procedure_code"));
            study.setModality(resultSet.getString("modality_code"));
            study.setStudyDate(resultSet.getString("study_dttm"));
            return study;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private RowMapper<Study>getMapper() {
        return  new StudyRowMapper();
    }


}
