package com.channel.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_CHANNEL_LISTING", uniqueConstraints = @UniqueConstraint(columnNames = {"channelId", "channelSkuId", "globalSkuId"}))
public class ChannelListing extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long globalSkuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelSkuId() {
        return channelSkuId;
    }

    public void setChannelSkuId(String channelSkuId) {
        this.channelSkuId = channelSkuId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }
}
