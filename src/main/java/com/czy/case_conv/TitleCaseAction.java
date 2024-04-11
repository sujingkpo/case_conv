package com.czy.case_conv;

import java.util.Optional;

public class TitleCaseAction extends SelectionReplacerAction {
    @Override
    protected String replace(String s) {
        return WordSplitConverter.convert(s, " ", (i, part) -> Optional.of(CamelCaseAction.ucfirst(part)));
    }
}
