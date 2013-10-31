package org.netbeans.modules.rtfcopypaste.options;

import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpinnerFontModel implements SpinnerModel {

    private final Integer DEFAULT_VALUE = 10;
    private final Integer MAX_VALUE = 40;
    private final Integer MIN_VALUE = 6;
    private Integer value = DEFAULT_VALUE;
    private ChangeListener listener;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Integer) value;
        if (listener != null) {
            listener.stateChanged(new ChangeEvent(value));
        }
    }

    @Override
    public Object getNextValue() {
        value = Math.min(value + 1, MAX_VALUE);
        if (listener != null) {
            listener.stateChanged(new ChangeEvent(value));
        }
        return value;
    }

    @Override
    public Object getPreviousValue() {
        value = Math.max(MIN_VALUE, value - 1);
        if (listener != null) {
            listener.stateChanged(new ChangeEvent(value));
        }
        return value;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        this.listener = l;
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        this.listener = null;
    }
}
