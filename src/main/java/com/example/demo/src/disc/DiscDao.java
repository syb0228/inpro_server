package com.example.demo.src.disc;

import com.example.demo.src.disc.model.GetDiscTestRes;
import com.example.demo.src.oauth.model.KakaoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DiscDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetDiscTestRes> getDiscTest(){
        String getDiscTestQuery = "select D.discIdx, name, discFeatureIdx, feature\n" +
                "from DiscFeature\n" +
                "inner join Disc D on DiscFeature.discIdx = D.discIdx\n" +
                "order by discFeatureIdx";

        return this.jdbcTemplate.query(getDiscTestQuery,
                (rs, rsNum) -> new GetDiscTestRes(
                        rs.getInt("discIdx"),
                        rs.getString("name"),
                        rs.getInt("discFeatureIdx"),
                        rs.getString("feature")
                ));
    }

    public int createUserDisc(int userIdx, double x, double y){
        String createUserDiscQuery = "insert into UserDisc (userIdx, x, y) VALUES (?, ?, ?);";
        Object[] createUserDiscParams = new Object[]{userIdx, x, y};
        this.jdbcTemplate.update(createUserDiscQuery, createUserDiscParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public void createUserDiscTestAsGood(int userDiscIdx, int discFeatureIdx){
        String createUserDiscTestAsGoodQuery = "insert into UserDiscTest (userDiscIdx, discFeatureIdx, fitOrNot) VALUES (?, ?, ?);";
        Object[] createUserDiscTestAsGoodParams = new Object[]{userDiscIdx, discFeatureIdx, "Y"};
        this.jdbcTemplate.update(createUserDiscTestAsGoodQuery, createUserDiscTestAsGoodParams);
    }

    public void createUserDiscTestAsBad(int userDiscIdx, int discFeatureIdx){
        String createUserDiscTestAsBadQuery = "insert into UserDiscTest (userDiscIdx, discFeatureIdx, fitOrNot) VALUES (?, ?, ?);";
        Object[] createUserDiscTestAsBadParams = new Object[]{userDiscIdx, discFeatureIdx, "N"};
        this.jdbcTemplate.update(createUserDiscTestAsBadQuery, createUserDiscTestAsBadParams);
    }

    public int createSearchDisc(int userIdx, double x, double y){
        String createSearchDiscQuery = "insert into SearchDisc (userIdx, x, y) VALUES (?, ?, ?);";
        Object[] createSearchDiscParams = new Object[]{userIdx, x, y};
        this.jdbcTemplate.update(createSearchDiscQuery, createSearchDiscParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

}