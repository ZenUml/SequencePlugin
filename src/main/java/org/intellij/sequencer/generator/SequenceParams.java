package org.intellij.sequencer.generator;

import org.intellij.sequencer.config.Configuration;
import org.intellij.sequencer.config.ExcludeEntry;
import org.intellij.sequencer.generator.filters.*;

import java.util.Iterator;
import java.util.List;

public class SequenceParams {
    private static final String PACKAGE_INDICATOR = ".*";
    private static final String RECURSIVE_PACKAGE_INDICATOR = ".**";

    private int _maxDepth = 3;
    private boolean smartInterface = true;
    private CompositeMethodFilter _methodFilter = new CompositeMethodFilter();
    private InterfaceImplFilter _implFilter = new InterfaceImplFilter();

    public SequenceParams() {
        List<ExcludeEntry> excludeList = Configuration.getInstance().getExcludeList();
        excludeList.stream().filter(ExcludeEntry::isEnabled).map(ExcludeEntry::getExcludeName).forEach(this::doSomething);
    }

    private void doSomething(String excludeName) {
        MethodFilter filter;
        if (excludeName.endsWith(PACKAGE_INDICATOR)) {
            int index = excludeName.lastIndexOf(PACKAGE_INDICATOR);
            filter = new PackageFilter(excludeName.substring(0, index));
        } else if (excludeName.endsWith(RECURSIVE_PACKAGE_INDICATOR)) {
            int index = excludeName.lastIndexOf(RECURSIVE_PACKAGE_INDICATOR);
            filter = new PackageFilter(excludeName.substring(0, index), true);
        } else {
            filter = new SingleClassFilter(excludeName);
        }
        _methodFilter.addFilter(filter);
    }

    int getMaxDepth() {
        return _maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this._maxDepth = maxDepth;
    }

    public boolean isSmartInterface() {
        return smartInterface;
    }

    public void setSmartInterface(boolean smartInterface) {
        this.smartInterface = smartInterface;
    }

    public CompositeMethodFilter getMethodFilter() {
        return _methodFilter;
    }

    public InterfaceImplFilter getInterfaceImplFilter() {
        return _implFilter;
    }
}

