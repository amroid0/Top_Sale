package com.aelzohry.topsaleqatar.utils.enumClasses;

import android.text.TextUtils;

import com.aelzohry.topsaleqatar.helper.Helper;
import com.aelzohry.topsaleqatar.model.LocalizedModel;
import com.aelzohry.topsaleqatar.model.StanderModel;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/01/2022.
 */
public enum CarColor {

    WHITE("white", "أبيض", "White"),
    BLACK("black", "أسود", "Black"),
    GREEN("green", "أخضر", "Green"),
    BLUE("blue", "أزرق", "Blue"),
    RED("red", "أحمر", "Red"),
    PINK("pink", "وردي", "Pink"),
    CYAN("cyan", "سماوي", "cyan"),
    BROWN("brown", "بني", "Brown"),
    BEIGE("beige", "بيج", "Beige"),
    YELLOW("yellow", "أصفر", "Yellow"),
    SILVER("silver", "سلفر", "Silver"),
    GOLDEN("golden", "ذهبي", "Golden"),
    NAVY_BLUE("navy_blue", "كحلي", "Navy blue"),
    PURPLE("purple", "بنفسجي", "Purple"),
    GRAY("gray", "سكني", "Gray"),
    BURGUNDY("burgundy", "عنابي", "Burgundy"),
    ANOTHER_COLOR("another_color", "لون آخر", "Another color");


    private String nameAr;
    private String nameEn;
    private String constant;


    CarColor(String constant, String nameAr, String nameEn) {
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.constant = constant;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getConstant() {
        return constant;
    }
    static ArrayList<StanderModel> yearList = new ArrayList<>();
    public static void populateYears(){
        for (int i = 2024; i >= 1800; i--) {
            yearList.add(new StanderModel(String.valueOf(i), String.valueOf(i),false));
        }

    }
    public static ArrayList<StanderModel>getYearList(){
        return yearList;
    }

   static ArrayList<StanderModel> list = new ArrayList<>();

    public static ArrayList<StanderModel> getList() {
         return list;
    }
    public static void populateList(){
        list.add(new StanderModel(WHITE.constant, new LocalizedModel(WHITE.nameAr, WHITE.nameEn).getLocalized(), false));
        list.add(new StanderModel(BLACK.constant, new LocalizedModel(BLACK.nameAr, BLACK.nameEn).getLocalized(), false));
        list.add(new StanderModel(GREEN.constant, new LocalizedModel(GREEN.nameAr, GREEN.nameEn).getLocalized(), false));
        list.add(new StanderModel(BLUE.constant, new LocalizedModel(BLUE.nameAr, BLUE.nameEn).getLocalized(), false));
        list.add(new StanderModel(RED.constant, new LocalizedModel(RED.nameAr, RED.nameEn).getLocalized(), false));
        list.add(new StanderModel(PINK.constant, new LocalizedModel(PINK.nameAr, PINK.nameEn).getLocalized(), false));
        list.add(new StanderModel(CYAN.constant, new LocalizedModel(CYAN.nameAr, CYAN.nameEn).getLocalized(), false));
        list.add(new StanderModel(BROWN.constant, new LocalizedModel(BROWN.nameAr, BROWN.nameEn).getLocalized(), false));
        list.add(new StanderModel(BEIGE.constant, new LocalizedModel(BEIGE.nameAr, BEIGE.nameEn).getLocalized(), false));
        list.add(new StanderModel(YELLOW.constant, new LocalizedModel(YELLOW.nameAr, YELLOW.nameEn).getLocalized(), false));
        list.add(new StanderModel(SILVER.constant, new LocalizedModel(SILVER.nameAr, SILVER.nameEn).getLocalized(), false));
        list.add(new StanderModel(GOLDEN.constant, new LocalizedModel(GOLDEN.nameAr, GOLDEN.nameEn).getLocalized(), false));
        list.add(new StanderModel(NAVY_BLUE.constant, new LocalizedModel(NAVY_BLUE.nameAr, NAVY_BLUE.nameEn).getLocalized(), false));
        list.add(new StanderModel(PURPLE.constant, new LocalizedModel(PURPLE.nameAr, PURPLE.nameEn).getLocalized(), false));
        list.add(new StanderModel(GRAY.constant, new LocalizedModel(GRAY.nameAr, GRAY.nameEn).getLocalized(), false));
        list.add(new StanderModel(BURGUNDY.constant, new LocalizedModel(BURGUNDY.nameAr, BURGUNDY.nameEn).getLocalized(), false));
        list.add(new StanderModel(ANOTHER_COLOR.constant, new LocalizedModel(ANOTHER_COLOR.nameAr, ANOTHER_COLOR.nameEn).getLocalized(), false));
    }

    public static String getTextByConstant(String constant) {
        if (TextUtils.isEmpty(constant)) return "";

        if (constant.equalsIgnoreCase(WHITE.constant)) {
            return Helper.INSTANCE.isEnglish() ? WHITE.nameEn : WHITE.nameAr;
        } else if (constant.equalsIgnoreCase(BLACK.constant)) {
            return Helper.INSTANCE.isEnglish() ? BLACK.nameEn : BLACK.nameAr;
        } else if (constant.equalsIgnoreCase(GREEN.constant)) {
            return Helper.INSTANCE.isEnglish() ? GREEN.nameEn : GREEN.nameAr;
        } else if (constant.equalsIgnoreCase(BLUE.constant)) {
            return Helper.INSTANCE.isEnglish() ? BLUE.nameEn : BLUE.nameAr;
        }else if (constant.equalsIgnoreCase(RED.constant)) {
            return Helper.INSTANCE.isEnglish() ? RED.nameEn : RED.nameAr;
        }else if (constant.equalsIgnoreCase(PINK.constant)) {
            return Helper.INSTANCE.isEnglish() ? PINK.nameEn : PINK.nameAr;
        }else if (constant.equalsIgnoreCase(CYAN.constant)) {
            return Helper.INSTANCE.isEnglish() ? CYAN.nameEn : CYAN.nameAr;
        }else if (constant.equalsIgnoreCase(BROWN.constant)) {
            return Helper.INSTANCE.isEnglish() ? BROWN.nameEn : BROWN.nameAr;
        }else if (constant.equalsIgnoreCase(BEIGE.constant)) {
            return Helper.INSTANCE.isEnglish() ? BEIGE.nameEn : BEIGE.nameAr;
        }else if (constant.equalsIgnoreCase(YELLOW.constant)) {
            return Helper.INSTANCE.isEnglish() ? YELLOW.nameEn : YELLOW.nameAr;
        }else if (constant.equalsIgnoreCase(SILVER.constant)) {
            return Helper.INSTANCE.isEnglish() ? SILVER.nameEn : SILVER.nameAr;
        }else if (constant.equalsIgnoreCase(GOLDEN.constant)) {
            return Helper.INSTANCE.isEnglish() ? GOLDEN.nameEn : GOLDEN.nameAr;
        }else if (constant.equalsIgnoreCase(NAVY_BLUE.constant)) {
            return Helper.INSTANCE.isEnglish() ? NAVY_BLUE.nameEn : NAVY_BLUE.nameAr;
        }else if (constant.equalsIgnoreCase(PURPLE.constant)) {
            return Helper.INSTANCE.isEnglish() ? PURPLE.nameEn : PURPLE.nameAr;
        }else if (constant.equalsIgnoreCase(GRAY.constant)) {
            return Helper.INSTANCE.isEnglish() ? GRAY.nameEn : GRAY.nameAr;
        }else if (constant.equalsIgnoreCase(BURGUNDY.constant)) {
            return Helper.INSTANCE.isEnglish() ? BURGUNDY.nameEn : BURGUNDY.nameAr;
        }else if (constant.equalsIgnoreCase(ANOTHER_COLOR.constant)) {
            return Helper.INSTANCE.isEnglish() ? ANOTHER_COLOR.nameEn : ANOTHER_COLOR.nameAr;
        }
        return constant;
    }

    public static StanderModel getObjectByConstant(String constant) {

        if (constant.equalsIgnoreCase(WHITE.constant)) {
            return new StanderModel(WHITE.constant, new LocalizedModel(WHITE.nameAr, WHITE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(BLACK.constant)) {
            return new StanderModel(BLACK.constant, new LocalizedModel(BLACK.nameAr, BLACK.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(GREEN.constant)) {
            return new StanderModel(GREEN.constant, new LocalizedModel(GREEN.nameAr, GREEN.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(BLUE.constant)) {
            return new StanderModel(BLUE.constant, new LocalizedModel(BLUE.nameAr, BLUE.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(RED.constant)) {
            return new StanderModel(RED.constant, new LocalizedModel(RED.nameAr, RED.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(PINK.constant)) {
            return new StanderModel(PINK.constant, new LocalizedModel(PINK.nameAr, PINK.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(CYAN.constant)) {
            return new StanderModel(CYAN.constant, new LocalizedModel(CYAN.nameAr, CYAN.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(BROWN.constant)) {
            return new StanderModel(BROWN.constant, new LocalizedModel(BROWN.nameAr, BROWN.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(BEIGE.constant)) {
            return new StanderModel(BEIGE.constant, new LocalizedModel(BEIGE.nameAr, BEIGE.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(YELLOW.constant)) {
            return new StanderModel(YELLOW.constant, new LocalizedModel(YELLOW.nameAr, YELLOW.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(SILVER.constant)) {
            return new StanderModel(SILVER.constant, new LocalizedModel(SILVER.nameAr, SILVER.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(GOLDEN.constant)) {
            return new StanderModel(GOLDEN.constant, new LocalizedModel(GOLDEN.nameAr, GOLDEN.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(NAVY_BLUE.constant)) {
            return new StanderModel(NAVY_BLUE.constant, new LocalizedModel(NAVY_BLUE.nameAr, NAVY_BLUE.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(PURPLE.constant)) {
            return new StanderModel(PURPLE.constant, new LocalizedModel(PURPLE.nameAr, PURPLE.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(GRAY.constant)) {
            return new StanderModel(GRAY.constant, new LocalizedModel(GRAY.nameAr, GRAY.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(BURGUNDY.constant)) {
            return new StanderModel(BURGUNDY.constant, new LocalizedModel(BURGUNDY.nameAr, BURGUNDY.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(ANOTHER_COLOR.constant)) {
            return new StanderModel(ANOTHER_COLOR.constant, new LocalizedModel(ANOTHER_COLOR.nameAr, ANOTHER_COLOR.nameEn).getLocalized(), false);
        }

        return null;
    }


}