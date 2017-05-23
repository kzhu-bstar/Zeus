package pig.dream.zeuslibs;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

/**
 * 控件满足条件enable启用，否则enbale false
 *
 * @author zhukun on 2017/3/20.
 */

public class WidgetEnableStatusHelper {

    public HashMap<EditText, IEditTextWidgetWatcher> editTextWatchers;
    public HashMap<CheckBox, ICheckBoxWidgetWatcher> checkBoxWatchers;
    private View targetView;

    public static WidgetEnableStatusHelper create() {
        return new WidgetEnableStatusHelper();
    }
    public WidgetEnableStatusHelper addEditTextWidget(EditText ed) {
        addEditTextWidget(ed, defaultEditTextWidgetWatcher);
        return this;
    }

    public WidgetEnableStatusHelper addEditTextWidget(EditText ed, IEditTextWidgetWatcher watcher) {
        if (editTextWatchers == null) {
            editTextWatchers = new HashMap<>();
        }
        editTextWatchers.put(ed, watcher);
        return this;
    }

    public WidgetEnableStatusHelper addCheckBoxWidget(CheckBox cb) {
        addCheckBoxWidget(cb, defaultCheckBoxWidgetWatcher);
        return this;
    }

    public WidgetEnableStatusHelper addCheckBoxWidget(CheckBox cb, ICheckBoxWidgetWatcher watcher) {
        if (checkBoxWatchers == null) {
            checkBoxWatchers = new HashMap<>();
        }
        checkBoxWatchers.put(cb, watcher);
        return this;
    }

    public WidgetEnableStatusHelper start(View view) {
        this.targetView = view;
        if (editTextWatchers != null) {
            for (Map.Entry<EditText, IEditTextWidgetWatcher> entry : editTextWatchers.entrySet()) {
                EditText ed = entry.getKey();
                ed.addTextChangedListener(textWatcher);
            }
        }
        if (checkBoxWatchers != null) {
            for (Map.Entry<CheckBox, ICheckBoxWidgetWatcher> entry : checkBoxWatchers.entrySet()) {
                CheckBox cb = entry.getKey();
                cb.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        }
        return this;
    }

    public void clean() {
        if (editTextWatchers != null) {
            editTextWatchers.clear();
        }
        if (checkBoxWatchers != null) {
            checkBoxWatchers.clear();
        }
        targetView = null;
    }

    private void watchAll() {
        boolean result = watchEditText() && watchCheckBox();

        this.targetView.setEnabled(result);
    }

    private boolean watchEditText() {
        if (checkBoxWatchers == null || checkBoxWatchers.size() <= 0) {
            return true;
        }
        boolean result = true;
        for (Map.Entry<EditText, IEditTextWidgetWatcher> entry : editTextWatchers.entrySet()) {
            Editable editable = entry.getKey().getText();
            IEditTextWidgetWatcher watch = entry.getValue();
            if (!watch.watch(editable)) {
                result = false;
                break;
            }
        }

        return result;
    }

    private boolean watchCheckBox() {
        if (checkBoxWatchers == null || checkBoxWatchers.size() <= 0) {
            return true;
        }
        boolean result = true;
        for (Map.Entry<CheckBox, ICheckBoxWidgetWatcher> entry : checkBoxWatchers.entrySet()) {
            CheckBox checkBox = entry.getKey();
            ICheckBoxWidgetWatcher watch = entry.getValue();
            if (!watch.watch(checkBox, checkBox.isChecked())) {
                result = false;
                break;
            }
        }

        return result;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            watchAll();
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            watchAll();
        }
    };

    private IEditTextWidgetWatcher defaultEditTextWidgetWatcher = new IEditTextWidgetWatcher() {

        @Override
        public boolean watch(Editable s) {
            return s.length() > 0;
        }
    };

    private ICheckBoxWidgetWatcher defaultCheckBoxWidgetWatcher = new ICheckBoxWidgetWatcher() {

        @Override
        public boolean watch(CompoundButton buttonView, boolean isChecked) {
            return isChecked;
        }
    };

    public interface IEditTextWidgetWatcher {
        boolean watch(Editable s);
    }

    public interface ICheckBoxWidgetWatcher {
        boolean watch(CompoundButton buttonView, boolean isChecked);
    }
}
