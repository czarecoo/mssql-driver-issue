package org.czareg.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilterSortNode {
    private Long id;
    private String name;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
    private String description;
    private Long version;

    private Node node;
}
