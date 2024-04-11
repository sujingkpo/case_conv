package com.czy.case_conv;

public class LowerCaseAction extends SelectionReplacerAction {
    @Override
    protected String replace(String s) {
        return s.toLowerCase();
    }
}
