package github.pancras.tcccore.dto;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TccActionContext implements Serializable {
    private String xid;
    // TccGlobal方法的第2个及之后的参数会被保存到map中，传递给commit和cancel1方法。
    private HashMap<String, Object> map = new HashMap<>();

    public void setContext(String key, Object val) {
        map.put(key, val);
    }

    public Object getContext(String key) {
        return map.get(key);
    }
}
