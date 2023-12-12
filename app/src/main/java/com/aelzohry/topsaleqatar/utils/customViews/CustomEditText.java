package com.aelzohry.topsaleqatar.utils.customViews;

import static android.text.InputType.TYPE_NULL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.databinding.CustomEditTextBinding;
import com.aelzohry.topsaleqatar.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputLayout;

/**
 * # Created by Mousa Hashihso on 10/03/2022.
 */
public class CustomEditText extends LinearLayoutCompat {

    private Context context;
    private AttributeSet attrs;
    private int defStyleAttr;

    private CustomEditTextBinding binding;
    private LayoutInflater mInflater = null;

    private Boolean showPasswordToggle = false;
    private Boolean showCountryCode = false;
    private Boolean showIcon = false;
    private Boolean showLabel = false;

    private Boolean editable = true;
    private Boolean isPassword = false;
    private Boolean isSingleLine = true;

    private String hint = "";
    private String text = "";
    private String error = "";
    private String label = "";

    private Drawable viewIcon;

    private int lines = 1;
    private int inputType = 0;
    private int gravity = 0;
    private int maxLength = 0;
    private int imeOptions = 0;

    private OnClickListener listener;
    private GestureDetector gestureDetector;


    public CustomEditText(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
//        gestureDetector = new GestureDetector(context, new SingleTapConfirm());
        int[] set = {
                android.R.attr.background, // idx 0
                android.R.attr.inputType,       // idx 1
                android.R.attr.text        // idx 2
        };
        // initiate inflater
        mInflater = LayoutInflater.from(context);
        binding = CustomEditTextBinding.inflate(mInflater, this, true);


        TypedArray customAttrArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCustomEditText, defStyleAttr, 0);
        showPasswordToggle = customAttrArray.getBoolean(R.styleable.MyCustomEditText_show_password_toggle, false);
        showCountryCode = customAttrArray.getBoolean(R.styleable.MyCustomEditText_show_country_code, false);
        showIcon = customAttrArray.getBoolean(R.styleable.MyCustomEditText_show_edit_text_icon, false);
        showLabel = customAttrArray.getBoolean(R.styleable.MyCustomEditText_show_label, false);
        isSingleLine = customAttrArray.getBoolean(R.styleable.MyCustomEditText_android_singleLine, true);
        isPassword = customAttrArray.getBoolean(R.styleable.MyCustomEditText_is_password, false);
        editable = customAttrArray.getBoolean(R.styleable.MyCustomEditText_editable, true);

        text = customAttrArray.getString(R.styleable.MyCustomEditText_text);
        hint = customAttrArray.getString(R.styleable.MyCustomEditText_hint);
        label = customAttrArray.getString(R.styleable.MyCustomEditText_label);
        error = customAttrArray.getString(R.styleable.MyCustomEditText_error);

        viewIcon = customAttrArray.getDrawable(R.styleable.MyCustomEditText_edit_text_icon_icon);


        lines = customAttrArray.getInt(R.styleable.MyCustomEditText_android_lines, 1);
        gravity = customAttrArray.getInt(R.styleable.MyCustomEditText_android_gravity, 0);
        maxLength = customAttrArray.getInt(R.styleable.MyCustomEditText_android_maxLength, 0);
        inputType = customAttrArray.getInt(R.styleable.MyCustomEditText_android_inputType, TYPE_NULL);
        imeOptions = customAttrArray.getInt(R.styleable.MyCustomEditText_android_imeOptions, 0);


        if (editable) {

            binding.etText.setVisibility(VISIBLE);
            binding.tvText.setVisibility(INVISIBLE);


            if (showPasswordToggle) {
                binding.inputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            } else {
                binding.inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
            }

            binding.etText.setHint(hint);
            binding.etText.setText(text);


            binding.etText.setOnFocusChangeListener((v, hasFocus) -> {

                if (hasFocus) {
                    binding.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                    binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text_selected));
                } else {
                    binding.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.text_color));
                    setError("");
                    binding.ivIcon.clearColorFilter();
                    binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text));
                }

                if (!TextUtils.isEmpty(error)) {
                    binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.ios_red), android.graphics.PorterDuff.Mode.SRC_IN);
                    binding.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.ios_red));

                }

//                if (TextUtils.isEmpty(error)) {
//                    if (hasFocus) {
//                        binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
//                        binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text_selected));
//                    } else {
//                        binding.ivIcon.clearColorFilter();
//                        binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text));
//                    }
//                }
            });


            binding.etText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    text = editable.toString();
                    if (!TextUtils.isEmpty(editable.toString())) {
                        setError("");
                    }
                }
            });
            binding.etText.setInputType(inputType);
            binding.etText.setLines(lines);

            if (gravity != 0)
                binding.etText.setGravity(lines);

            if (maxLength != 0)
                binding.etText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

            if (imeOptions != 0)
                binding.etText.setImeOptions(imeOptions);

            binding.etText.setSingleLine(isSingleLine);

            if (isPassword) {
                binding.etText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                binding.etText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

        } else {
            binding.etText.setVisibility(INVISIBLE);
            binding.tvText.setVisibility(VISIBLE);

            binding.tvText.setHint(hint);
            binding.tvText.setText(text);
            binding.tvText.setLines(lines);

            if (gravity != 0)
                binding.tvText.setGravity(lines);

            if (maxLength != 0)
                binding.tvText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

            binding.tvText.setSingleLine(isSingleLine);
        }

        binding.tvCountryCode.setVisibility(showCountryCode ? VISIBLE : GONE);
        binding.ivIcon.setVisibility(showIcon ? VISIBLE : GONE);
        if (viewIcon != null) {
            binding.ivIcon.setImageDrawable(viewIcon);
        }

        binding.tvLabel.setVisibility(showLabel ? VISIBLE : GONE);
        binding.tvLabel.setText(label);

        setError(error);


        customAttrArray.recycle();

    }


    private void setData() {
        init(context, attrs, defStyleAttr);
    }

    public String getText() {
        if (editable) {
//            return text;
            return ValidationUtils.getText(binding.etText);
        } else {
            return ValidationUtils.getText(binding.tvText);
        }
    }


    public EditText getEditText() {
        return binding.etText;
    }

    public TextView getTextView() {
        return binding.tvText;
    }

    public void removeEditTextFocus() {
        binding.etText.clearFocus();
    }

    public void setError(String error) {
        this.error = error;
        binding.tvError.setText(error);

//        binding.inputLayout.setError(null);
//        binding.inputLayout.setErrorEnabled(false);

        if (TextUtils.isEmpty(error)) {
            binding.llError.setVisibility(INVISIBLE);
            binding.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.text_color));

            if (binding.etText.hasFocus()) {
                binding.ivIcon.clearColorFilter();
                binding.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text_selected));
            } else {
//                binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.red_color), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text));
            }
        } else {
            binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.ios_red), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.ios_red));
            binding.etText.requestFocus();
            binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text_error));
            binding.llError.setVisibility(VISIBLE);

//            binding.inputLayout.setError(error);
//            binding.inputLayout.setErrorEnabled(false);
        }
    }

    public void setText(String text) {
        this.text = text;
        setError("");
        if (editable) {
            binding.etText.setText(text);
        } else {
            binding.tvText.setText(text);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        if (editable) {
            binding.etText.setOnClickListener(listener);
        } else {
            binding.llEditTextContainer.setOnTouchListener((view, motionEvent) -> {


//                if (gestureDetector.onTouchEvent(arg1)) {
//                    // single tap
//                    return true;
//                } else {
//                    // your code for move and drag
//                }
//
//                return false;

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    new Handler().postDelayed(() -> listener.onClick(view), 500);

                    binding.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                    binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text_selected));
                    // Do what you want
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    binding.ivIcon.clearColorFilter();
                    binding.llEditTextContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_bg_edit_text));
                }
                return false;
            });

        }
    }

//    @Override
//    public void invalidate() {
//        setData();
//        super.invalidate();
//    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}
