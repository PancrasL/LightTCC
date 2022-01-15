package github.pancras.tcccore.dto;

import java.io.Serializable;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TccActionContext implements Serializable {
    private String xid;
    private HashMap<String, String> map;

    public String getContext(String key) {
        return map.get(key);
    }
}
