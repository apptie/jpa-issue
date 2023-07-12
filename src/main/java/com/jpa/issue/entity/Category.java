package com.jpa.issue.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * parent 1 : N child
 */
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * children이 필드로 존재
     * children을 바라보고 있는 상태
     * 그러므로 Category는 parent
     * 그러므로 @OneToMany
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    public List<Category> children = new ArrayList<>();

    /**
     * parent가 필드로 존재
     * parent를 바라보고 있는 상태
     * 그러므로 Category는 children
     * 그러므로 @ManyToOne
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Category parent;

    protected Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public void initChild(Category child) {
        this.children.add(child);
        child.parent = this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public Category getParent() {
        return parent;
    }
}
