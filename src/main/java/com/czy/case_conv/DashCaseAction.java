package com.czy.case_conv;

import java.util.Optional;

public class DashCaseAction extends SelectionReplacerAction {
    @Override
    protected String replace(String s) {
        return WordSplitConverter.convert(s, "-", (i, s1) -> Optional.of(s1.toLowerCase()));
    }
}
