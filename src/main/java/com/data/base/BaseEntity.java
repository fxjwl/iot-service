package com.data.base;

import com.data.util.DateTimeUtil;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    public static final String COLUMN_CREATE_TIME = "createTime";
    public static final String COLUMN_LASTMODIFYTIME = "lastModifyTime";
    public static final String COLUMN_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUIDGenerater")
    @GenericGenerator(name = "UUIDGenerater",strategy = "com.data.base.UUIDGenerater")
    @Column(name = "id", length = 32)
    protected String id;

    @CreatedDate
    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @LastModifiedDate
    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @Column(name = "last_modify_time", nullable = false)
    private Date lastModifyTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return this.lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
