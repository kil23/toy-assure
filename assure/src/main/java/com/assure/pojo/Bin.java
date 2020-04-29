package com.assure.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_BIN")
public class Bin extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    @SequenceGenerator(name="seq", initialValue=1001, allocationSize=100)
    private Long binId;

    public Long getBinId() {
        return binId;
    }
    public void setBinId(Long binId) {
        this.binId = binId;
    }
}
