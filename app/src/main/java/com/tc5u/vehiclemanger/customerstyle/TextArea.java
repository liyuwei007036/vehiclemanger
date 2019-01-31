package com.tc5u.vehiclemanger.customerstyle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc5u.vehiclemanger.R;

/**
 * ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
 * ┃   ╭﹉﹊﹉╮ ╔═════╗╔═════╗╔═════╗┃
 * ┃╭╯老斯基㊣╠╣淡漠de人生╠╣2016-10-09╠╣ 欢迎关注 ╣┃
 * ┃╰⊙┈┈┈⊙╯╚◎═══◎╝╚◎═══◎╝╚◎═══◎╝┃
 * ┣━━━┳━━━━━━━━━━━━━━━━━━━━━━━━┫
 * ┃GitHub┃https://github.com/FJ917                        ┃
 * ┃简  书┃http://www.jianshu.com/users/3d2770e6e489       ┃
 * ┃Q    Q┃511334686                                       ┃
 * ┗━━━┻━━━━━━━━━━━━━━━━━━━━━━━━┛
 */

public class TextArea extends RelativeLayout {

    //类型1(单数类型)：TextView显示总字数，然后根据输入递减.例：100，99，98
    //类型2(百分比类型)：TextView显示总字数和当前输入的字数，例：0/100，1/100，2/100
    public static final String SINGULAR = "Singular";//类型1(单数类型)
    public static final String PERCENTAGE = "Percentage";//类型2(百分比类型)
    private EditText etContent;//文本框
    private TextView tvNum;//字数显示TextView
    private View vLineUp;//底部横线
    private View vLineDn;//底部横线
    private String TYPES = SINGULAR;//类型
    private int MaxNum = 100;//最大字符
    private String hint = "请输入内容";//提示文字
    private int MinHeight = 100;//最小高度
    private int LineColor = Color.BLACK;//横线颜色
    private int TextColor = Color.BLACK;//输入文字颜色
    private String text = "";//默认文字

    public TextArea(Context context) {
        this(context, null);
    }

    public TextArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.textarea, this, true);
        etContent = findViewById(R.id.etContent);
        tvNum = findViewById(R.id.tvNum);
        vLineUp = findViewById(R.id.vLineUp);
        vLineDn = findViewById(R.id.vLineDn);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextArea);
        if (typedArray != null) {
            //默认文字
            text = typedArray.getString(R.styleable.TextArea_etText);
            etContent.setText(text);
            etContent.setSelection(etContent.getText().length());
            //提示文字
            hint = typedArray.getString(R.styleable.TextArea_etHint);
            etContent.setHint(hint);
            //提示文字颜色
            etContent.setHintTextColor(typedArray.getColor(R.styleable.TextArea_etHintColor, Color.rgb(155, 155, 155)));
            //最小高度
            etContent.setMinHeight(px2dip(context,
                    typedArray.getDimensionPixelOffset(R.styleable.TextArea_etMinHeight, 200)));
            //最大字符
            MaxNum = typedArray.getInt(R.styleable.TextArea_etMaxLength, 100);
            //横线颜色
            LineColor = typedArray.getColor(R.styleable.TextArea_etLineColor, Color.BLACK);
            vLineDn.setBackgroundColor(LineColor);
            vLineUp.setBackgroundColor(LineColor);
            //输入文字大小
            etContent.setTextSize(px2sp(context,
                    typedArray.getDimensionPixelOffset(R.styleable.TextArea_etTextSize, 16)));
            //输入文字颜色
            TextColor = typedArray.getColor(R.styleable.TextArea_etTextColor, Color.BLACK);
            etContent.setTextColor(TextColor);
            //设置提示统计文字大小
            tvNum.setTextSize(px2sp(context,
                    typedArray.getDimensionPixelSize(R.styleable.TextArea_etPromptTextSize, 12)));
            //设置提示统计文字颜色
            tvNum.setTextColor(typedArray.getColor(R.styleable.TextArea_etPromptTextColor, Color.BLACK));
            //设置提示统计显示类型
            int t = typedArray.getInt(R.styleable.TextArea_etType, 0);
            if (t == 0) TYPES = SINGULAR;
            else TYPES = PERCENTAGE;
            if (TYPES.equals(SINGULAR)) {//类型1
                tvNum.setText(String.valueOf(MaxNum));
            } else if (TYPES.equals(PERCENTAGE)) {//类型2
                tvNum.setText(0 + "/" + MaxNum);
            }
            //设置提示位置
            int promptPosition = typedArray.getInt(R.styleable.TextArea_etPromptPosition, 0);
            if (promptPosition == 0) {//上方
                vLineDn.setVisibility(View.VISIBLE);
                vLineUp.setVisibility(View.GONE);
            } else {//下方
                vLineUp.setVisibility(View.VISIBLE);
                vLineDn.setVisibility(View.GONE);
            }
            typedArray.recycle();
        }
        //设置长度
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MaxNum)});
        //监听输入
        etContent.addTextChangedListener(mTextWatcher);
        setLeftCount();
    }

    private int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    private static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置显示
     *
     * @return
     */
    @Deprecated
    public TextArea show() {
        if (TYPES.equals(SINGULAR)) {//类型1
            tvNum.setText(String.valueOf(MaxNum));
        } else if (TYPES.equals(PERCENTAGE)) {//类型2
            tvNum.setText(0 + "/" + MaxNum);
        }
        //设置长度
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MaxNum)});
        //监听输入
        etContent.addTextChangedListener(mTextWatcher);
        return this;
    }

    /**
     * 设置横线颜色
     *
     * @param color --颜色值
     * @return
     */
    @Deprecated
    public TextArea setLineColor(String color) {
        vLineUp.setBackgroundColor(Color.parseColor(color));
        vLineDn.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    /**
     * 设置类型
     *
     * @param type --类型
     * @return
     */
    @Deprecated
    public TextArea setType(String type) {
        TYPES = type;
        return this;
    }

    /**
     * 设置最大字数
     *
     * @param num --字数
     * @return
     */
    @Deprecated
    public TextArea setLength(int num) {
        this.MaxNum = num;
        return this;
    }

    /**
     * 设置文本框的Hint
     *
     * @param str --设置内容
     * @return
     */
    @Deprecated
    public TextArea setEtHint(String str) {
        etContent.setHint(str);
        return this;
    }

    /**
     * 设置文本框的最小高度
     *
     * @param px --最小高度(单位px)
     * @return
     */
    @Deprecated
    public TextArea setEtMinHeight(int px) {
        etContent.setMinHeight(px);
        return this;
    }

    /**
     * 获取输入内容
     *
     * @return 内容
     */
    public String getText() {
        return etContent.getText().toString();
    }

    /**
     * 设置默认内容
     *
     * @param str --内容
     */
    public void setText(String str) {
        etContent.setText(str);
        etContent.setSelection(etContent.getText().length());
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = etContent.getSelectionStart();
            editEnd = etContent.getSelectionEnd();
            // 先去掉监听器，否则会出现栈溢出
            etContent.removeTextChangedListener(mTextWatcher);
            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MaxNum) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // 恢复监听器
            etContent.addTextChangedListener(mTextWatcher);
            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    /**
     * 刷新剩余输入字数
     */
    private void setLeftCount() {
        if (TYPES.equals(SINGULAR)) {//类型1
            tvNum.setText(String.valueOf((MaxNum - getInputCount())));
        } else if (TYPES.equals(PERCENTAGE)) {//类型2
            tvNum.setText(MaxNum - (MaxNum - getInputCount()) + "/" + MaxNum);
        }

    }

    /**
     * 获取用户输入内容字数
     */
    private long getInputCount() {
        return calculateLength(etContent.getText().toString());
    }

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param cs
     * @return
     */
    public static long calculateLength(CharSequence cs) {
        double len = 0;
        for (int i = 0; i < cs.length(); i++) {
            int tmp = (int) cs.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 1;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }
}
