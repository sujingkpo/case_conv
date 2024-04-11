package com.czy.case_conv;

public class UpperCaseAction extends SelectionReplacerAction {
    @Override
    protected String replace(String s) {
        return s.toUpperCase();
    }
}
