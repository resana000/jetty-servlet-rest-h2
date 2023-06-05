package com.servlet.h2;

import com.servlet.dto.FileInfo;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.List;

@Data
public class FileRepository {

    private JdbcTemplate jtm;

    private final String INSERT_FILE = "insert into file_info (name, size, type) values (?, ?, ?)";
    private final String FIND_ALL = "select * from file_info";
    private final String FIND_BY_ID = "select * from file_info where id = ?";

    public FileRepository() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl("jdbc:h2:mem:test;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1");
        this.jtm = new JdbcTemplate(ds);
    }


    public FileInfo getById(Long id) {
        return jtm.queryForObject(FIND_BY_ID, (rs, rowNum) ->
                new FileInfo(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("size"),
                        rs.getString("type")), new Object[]{id});
    }

    public List<FileInfo> getAll() {
        return jtm.query(FIND_ALL, (rs, rowNum) ->
                new FileInfo(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("size"),
                        rs.getString("type")));
    }

    public FileInfo create(FileInfo fileInfo) {
        jtm.update(INSERT_FILE, fileInfo.getName(), fileInfo.getSize(), fileInfo.getType());
        return fileInfo;
    }
}
