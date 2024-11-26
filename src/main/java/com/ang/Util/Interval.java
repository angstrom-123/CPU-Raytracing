package com.ang.Util;

import com.ang.Global;

public class Interval {
    public double min, max;

    // default
    public Interval() {
        min = -Global.infinity;
        max = Global.infinity;
    }

    // enclose 2 points
    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    // enclose 2 intervals
    public Interval(Interval a, Interval b) {
        if (a.min < b.min) {
            min = a.min;
        } else {
            min = b.min;
        }

        if (a.max > b.max) {
            max = a.max;
        } else {
            max = b.max;
        }
    }

    public double size() {
        return max - min;
    }

    public boolean contains(double x) {
        return (min <= x && x <= max);
    }

    public boolean surrounds(double x) {
        return (min < x && x < max);
    }

    public double clamp(double x) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }

    public Interval expand(double delta) {
        double padding = delta / 2;
        return new Interval(min - padding, max + padding);
    }

    public void setMin(double t) {
        min = t;
    }

    public void setMax(double t) {
        max = t;
    }

    public static Interval empty() {
        return new Interval(Global.infinity, -Global.infinity);
    }

    public static Interval universe() {
        return new Interval(-Global.infinity, Global.infinity);
    }
}
