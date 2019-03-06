package skjinnero.com.recommendation.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class HistoryPrimaryKey implements Serializable {
    private String user_id;

    private String item_id;
}
