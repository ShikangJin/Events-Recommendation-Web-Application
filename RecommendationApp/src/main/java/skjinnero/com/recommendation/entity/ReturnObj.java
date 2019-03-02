package skjinnero.com.recommendation.entity;

import java.util.List;

public class ReturnObj {
    List<Item> arr;
    public ReturnObj(List<Item> arr) {
        this.arr = arr;
    }

    // controller里return ReturnObj为json data给前端时，必须有get方法 名字getXX中XX会为返回的data的key
    public List<Item> getItems() {
        return this.arr;
    }

    public void setter(List<Item> list) {
        this.arr = arr;
    }
}
