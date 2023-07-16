package com.jpa.issue.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Region implements Persistable<String> {

    @Id
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_region_id")
    private Region firstRegion;

    @OneToMany(mappedBy = "firstRegion", cascade = CascadeType.PERSIST)
    private List<Region> secondRegions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_region_id")
    private Region secondRegion;

    @OneToMany(mappedBy = "secondRegion", cascade = CascadeType.PERSIST)
    private List<Region> thirdRegions = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    protected Region() {
    }

    public Region(String name) {
        this.name = name;
    }

    public Region(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void initSecondRegion(Region secondRegion) {
        this.secondRegions.add(secondRegion);
        secondRegion.firstRegion = this;
    }

    public void initThirdRegion(Region thirdRegion) {
        this.thirdRegions.add(thirdRegion);
        thirdRegion.secondRegion = this;
        thirdRegion.firstRegion = this.firstRegion;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return this.createdDate == null;
    }

    public String getName() {
        return name;
    }

    public Region getFirstRegion() {
        return firstRegion;
    }

    public List<Region> getSecondRegions() {
        return secondRegions;
    }

    public Region getSecondRegion() {
        return secondRegion;
    }

    public List<Region> getThirdRegions() {
        return thirdRegions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region region = (Region) o;
        return Objects.equals(getId(), region.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
