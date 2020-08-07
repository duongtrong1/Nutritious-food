package com.spring.dev2chuc.nutritious_food.model;

public enum ExerciseIntensity {
    LIGHT_ACTIVITY(1.2),
    LIGHT_MANUAL_LIMBS(1.4),
    MODERATE_ACTIVITY(1.6),
    ACTIVE_MUCH(1.8),
    WORKS_A_LOT(2.0);

    private double value;

    ExerciseIntensity(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static boolean hasValue(double value) {
        for (ExerciseIntensity _this : ExerciseIntensity.values()) {
            if (_this.getValue() == value)
                return true;
        }
        return false;
    }
}
