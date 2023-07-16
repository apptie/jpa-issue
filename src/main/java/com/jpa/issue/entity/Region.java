package com.jpa.issue.entity;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
