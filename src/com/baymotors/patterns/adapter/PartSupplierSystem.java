package com.baymotors.patterns.adapter;

import com.baymotors.models.Part;

import java.util.List;

public interface PartSupplierSystem {
    List<Part> getAvailableParts();
    boolean orderPart(String code, int quantity);
}
