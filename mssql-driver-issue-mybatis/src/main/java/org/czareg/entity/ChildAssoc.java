package org.czareg.entity;

import lombok.Data;

@Data
public class ChildAssoc {
    private Long id;
    private Node parent;
    private Node child;
}
