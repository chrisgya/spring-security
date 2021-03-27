package com.chrisgya.springsecurity.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T> {
    protected static final String SINGLE_RESULT = "single";
    protected static final String MULTIPLE_RESULT = "list";
    protected static final String RESULT_COUNT = "count";
    protected JdbcTemplate jdbcTemplate;
    protected SimpleJdbcCall create;
    protected SimpleJdbcCall update;
    protected SimpleJdbcCall delete;
    protected SimpleJdbcCall find;
    protected SimpleJdbcCall findAll;
    protected SimpleJdbcCall findAllWithParam;

    public AbstractDao() {
    }

    public abstract void setDataSource(DataSource var1);

    public <T1, T2> T2 create(T1 model, Class<T2> T2) throws DataAccessException {
        SqlParameterSource in = new CustomPropertySqlParameterSource(model);
        Map<String, Object> m = this.create.execute(in);
        T2 id = (T2) m.get("id");
        return id;
    }

    public <T> int update(T model) throws DataAccessException {
        SqlParameterSource in = new CustomPropertySqlParameterSource(model);
        Map<String, Object> m = this.update.execute(in);
        return  (int) m.get("RETURN_VALUE");
    }


    public <T> int delete(T id) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id);
        Map<String, Object> m =this.delete.execute(in);
        return (Integer) m.get("RETURN_VALUE");
    }


    public <T1, T2> T2 find(T1 id) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id);
        Map<String, Object> m = this.find.execute(in);
        List<T> result = (List) m.get("single");
        return !result.isEmpty() ? (T2) result.get(0) : null;
    }

//    public List<T> findAll() throws DataAccessException {
//        return this.findAll(0, 0, false).getContent();
//    }
//
//    public Page<T> findAll(Integer pageNum, Integer pageSize, boolean includeCount) throws DataAccessException {
//
//        SqlParameterSource in = (new MapSqlParameterSource()).addValue("page_num", pageNum).addValue("page_size", pageSize == 0 ? null : pageSize);
//        Map<String, Object> m = this.findAll.execute(in);
//        List<T> content = (List) m.get("list");
//        Long count = 0L;
//        if(includeCount) {
//            count = (Long) ((List) m.get("count")).get(0);
//        }
//        return new Page(count, content);
//    }

    public <T1, T2> List<T2> findAllWithParam(String idParam, T1 id) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue(idParam, id);
        Map<String, Object> m = this.findAllWithParam.execute(in);
        List<T2> content = (List) m.get("list");
        return content;
    }

    public class RowCountMapper implements RowMapper {
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }
    }
}
