package com.baymotors.patterns.adapter;

import com.baymotors.models.Part;
import com.baymotors.models.Supplier;

import java.util.List;

public class PartSupplierAdapter implements PartSupplierSystem {
    private Supplier supplier;

    public PartSupplierAdapter(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public List<Part> getAvailableParts() {
        return supplier.getSuppliedParts();
    }

    @Override
    public boolean orderPart(String code, int quantity) {
        return supplier.order(code, quantity);
    }
}
