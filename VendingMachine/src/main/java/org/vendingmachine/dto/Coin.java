package org.vendingmachine.dto;

import java.math.BigDecimal;

public enum Coin {
    QUARTER(new BigDecimal("0.25")), DIME(new BigDecimal("0.10")), NICKEL(new BigDecimal("0.05")), PENNY(new BigDecimal("0.01"));

    private final BigDecimal value;

    Coin(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String toString(){
        switch (this) {
            case QUARTER:
                return "0.25";
            case DIME:
                return "0.10";
            case NICKEL:
                return "0.05";
            case PENNY:
                return "0.01";
        }
        return null;
    }

}
