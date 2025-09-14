package org.czareg.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Node {
    private Long id;
    private String name;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
    private String description;
    private Long version;
}
