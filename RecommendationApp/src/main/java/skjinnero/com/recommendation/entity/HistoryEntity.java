package skjinnero.com.recommendation.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Source;
import org.hibernate.annotations.SourceType;

import javax.persistence.*;

@Entity
@Table(name="history")
@Data
@IdClass(HistoryPrimaryKey.class)
@DynamicUpdate
public class HistoryEntity {


    @Id
    @GeneratedValue
    @Column(name="user_id")
    private String user_id;


    @Id
    @GeneratedValue
    @Column(name="item_id")
    private String item_id;

    @Version
    @Column(name = "timestamp",columnDefinition="timestamp")
    @Source(value = SourceType.DB)
    private java.sql.Timestamp timestamp;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
