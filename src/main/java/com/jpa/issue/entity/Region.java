package com.jpa.issue.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Region {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_region_id")
    private Region firstRegion;

    @OneToMany(mappedBy = "firstRegion", cascade = CascadeType.PERSIST)
    private List<Region> secondRegions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_region_id")
    private Region secondRegion;

    @OneToMany(mappedBy = "secondRegion", cascade = CascadeType.PERSIST)
    private List<Region> thirdRegions;

    protected Region() {
    }

    public Region(String name) {
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

    public Long getId() {
        return id;
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
}
