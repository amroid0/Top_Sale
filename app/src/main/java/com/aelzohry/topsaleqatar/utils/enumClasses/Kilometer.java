package com.aelzohry.topsaleqatar.utils.enumClasses;

import android.text.TextUtils;

import com.aelzohry.topsaleqatar.helper.Helper;
import com.aelzohry.topsaleqatar.model.LocalizedModel;
import com.aelzohry.topsaleqatar.model.StanderModel;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/01/2022.
 */
public enum Kilometer {

    ZEROES("000", "أصفار", "Zeros"),
    FROM_ZERO_TO_9999("0_9999", "0 إلى 9999", "0 to 9999"),
    FROM_10000_TO_19999("10000_19999", "10000 إلى 19999", "10000 to 19999"),
    FROM_20000_TO_29999("20000_29999", "20000 إلى 29999", "20000 to 29999"),
    FROM_30000_TO_39999("30000_39999", "30000 إلى 39999", "30000 to 39999"),
    FROM_40000_TO_49999("40000_49999", "40000 إلى 49999", "40000 to 49999"),
    FROM_50000_TO_59999("50000_59999", "50000 إلى 59999", "50000 to 59999"),
    FROM_60000_TO_69999("60000_69999", "60000 إلى 69999", "60000 to 69999"),
    FROM_70000_TO_79999("70000_79999", "70000 إلى 79999", "70000 to 79999"),
    FROM_80000_TO_89999("80000_89999", "80000 إلى 89999", "80000 to 89999"),
    FROM_90000_TO_99999("90000_99999", "90000 إلى 99999", "90000 to 99999"),
    FROM_100000_TO_109999("100000_109999", "100000 إلى 109999", "100000 to 109999"),
    FROM_110000_TO_119999("110000_119999", "110000 إلى 119999", "110000 to 119999"),
    FROM_120000_TO_129999("120000_129999", "120000 إلى 129999", "120000 to 129999"),

    FROM_130000_TO_139999("130000_139999", "130000 إلى 139999", "130000 to 139999"),
    FROM_140000_TO_149999("140000_149999", "140000 إلى 149999", "140000 to 149999"),
    FROM_150000_TO_159999("150000_159999", "150000 إلى 159999", "150000 to 159999"),
    FROM_160000_TO_169999("160000_169999", "160000 إلى 169999", "160000 to 169999"),
    FROM_170000_TO_179999("170000_179999", "170000 إلى 179999", "170000 to 179999"),
    FROM_180000_TO_189999("180000_189999", "180000 إلى 189999", "180000 to 189999"),
    FROM_190000_TO_199999("190000_199999", "190000 إلى 199999", "190000 to 199999"),
    FROM_MORE_200000("more_than_200000", "أكثر من 200000", "More than 200000");


    private String nameAr;
    private String nameEn;
    private String constant;

    Kilometer(String constant, String nameAr, String nameEn) {
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


//    public ArrayList<Kilometer> getList() {
//        ArrayList<Kilometer> list = new ArrayList<>();
//        list.add(FROM_ZERO_TO_9999);
//        list.add(FROM_10000_TO_19999);
//        list.add(FROM_20000_TO_29999);
//        list.add(FROM_30000_TO_39999);
//        list.add(FROM_40000_TO_49999);
//        list.add(FROM_50000_TO_59999);
//        list.add(FROM_60000_TO_69999);
//        list.add(FROM_70000_TO_79999);
//        list.add(FROM_80000_TO_89999);
//        list.add(FROM_90000_TO_99999);
//        list.add(FROM_100000_TO_109999);
//        list.add(FROM_110000_TO_119999);
//        list.add(FROM_120000_TO_129999);
//
//        list.add(FROM_120000_TO_129999);
//        list.add(FROM_130000_TO_139999);
//        list.add(FROM_140000_TO_149999);
//        list.add(FROM_150000_TO_159999);
//        list.add(FROM_160000_TO_169999);
//        list.add(FROM_170000_TO_179999);
//        list.add(FROM_180000_TO_189999);
//        list.add(FROM_190000_TO_199999);
//        list.add(FROM_MORE_200000);
//
//
//        return list;
//    }


    public static ArrayList<StanderModel> getList() {
        ArrayList<StanderModel> list = new ArrayList<>();
        list.add(new StanderModel(ZEROES.constant, new LocalizedModel(ZEROES.nameAr, ZEROES.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_ZERO_TO_9999.constant, new LocalizedModel(FROM_ZERO_TO_9999.nameAr, FROM_ZERO_TO_9999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_10000_TO_19999.constant, new LocalizedModel(FROM_10000_TO_19999.nameAr, FROM_10000_TO_19999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_20000_TO_29999.constant, new LocalizedModel(FROM_20000_TO_29999.nameAr, FROM_20000_TO_29999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_30000_TO_39999.constant, new LocalizedModel(FROM_30000_TO_39999.nameAr, FROM_30000_TO_39999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_40000_TO_49999.constant, new LocalizedModel(FROM_40000_TO_49999.nameAr, FROM_40000_TO_49999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_50000_TO_59999.constant, new LocalizedModel(FROM_50000_TO_59999.nameAr, FROM_50000_TO_59999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_60000_TO_69999.constant, new LocalizedModel(FROM_60000_TO_69999.nameAr, FROM_60000_TO_69999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_70000_TO_79999.constant, new LocalizedModel(FROM_70000_TO_79999.nameAr, FROM_70000_TO_79999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_80000_TO_89999.constant, new LocalizedModel(FROM_80000_TO_89999.nameAr, FROM_80000_TO_89999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_90000_TO_99999.constant, new LocalizedModel(FROM_90000_TO_99999.nameAr, FROM_90000_TO_99999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_100000_TO_109999.constant, new LocalizedModel(FROM_100000_TO_109999.nameAr, FROM_100000_TO_109999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_110000_TO_119999.constant, new LocalizedModel(FROM_110000_TO_119999.nameAr, FROM_110000_TO_119999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_120000_TO_129999.constant, new LocalizedModel(FROM_120000_TO_129999.nameAr, FROM_120000_TO_129999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_130000_TO_139999.constant, new LocalizedModel(FROM_130000_TO_139999.nameAr, FROM_130000_TO_139999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_140000_TO_149999.constant, new LocalizedModel(FROM_140000_TO_149999.nameAr, FROM_140000_TO_149999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_150000_TO_159999.constant, new LocalizedModel(FROM_150000_TO_159999.nameAr, FROM_150000_TO_159999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_160000_TO_169999.constant, new LocalizedModel(FROM_160000_TO_169999.nameAr, FROM_160000_TO_169999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_170000_TO_179999.constant, new LocalizedModel(FROM_170000_TO_179999.nameAr, FROM_170000_TO_179999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_180000_TO_189999.constant, new LocalizedModel(FROM_180000_TO_189999.nameAr, FROM_180000_TO_189999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_190000_TO_199999.constant, new LocalizedModel(FROM_190000_TO_199999.nameAr, FROM_190000_TO_199999.nameEn).getLocalized(), false));
        list.add(new StanderModel(FROM_MORE_200000.constant, new LocalizedModel(FROM_MORE_200000.nameAr, FROM_MORE_200000.nameEn).getLocalized(), false));

        return list;
    }


    public static String getTextByConstant(String constant) {
        if (TextUtils.isEmpty(constant)) return "";
        if (constant.equalsIgnoreCase(ZEROES.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? ZEROES.nameEn : ZEROES.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_ZERO_TO_9999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_ZERO_TO_9999.nameEn : FROM_ZERO_TO_9999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_10000_TO_19999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_10000_TO_19999.nameEn : FROM_10000_TO_19999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_20000_TO_29999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_20000_TO_29999.nameEn : FROM_20000_TO_29999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_30000_TO_39999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_30000_TO_39999.nameEn : FROM_30000_TO_39999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_40000_TO_49999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_40000_TO_49999.nameEn : FROM_40000_TO_49999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_50000_TO_59999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_50000_TO_59999.nameEn : FROM_50000_TO_59999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_60000_TO_69999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_60000_TO_69999.nameEn : FROM_60000_TO_69999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_70000_TO_79999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_70000_TO_79999.nameEn : FROM_70000_TO_79999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_80000_TO_89999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_80000_TO_89999.nameEn : FROM_80000_TO_89999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_90000_TO_99999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_90000_TO_99999.nameEn : FROM_90000_TO_99999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_100000_TO_109999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_100000_TO_109999.nameEn : FROM_100000_TO_109999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_110000_TO_119999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_110000_TO_119999.nameEn : FROM_110000_TO_119999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_120000_TO_129999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_120000_TO_129999.nameEn : FROM_120000_TO_129999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_130000_TO_139999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_130000_TO_139999.nameEn : FROM_130000_TO_139999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_140000_TO_149999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_140000_TO_149999.nameEn : FROM_140000_TO_149999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_150000_TO_159999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_150000_TO_159999.nameEn : FROM_150000_TO_159999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_160000_TO_169999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_160000_TO_169999.nameEn : FROM_160000_TO_169999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_170000_TO_179999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_170000_TO_179999.nameEn : FROM_170000_TO_179999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_180000_TO_189999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_180000_TO_189999.nameEn : FROM_180000_TO_189999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_190000_TO_199999.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_190000_TO_199999.nameEn : FROM_190000_TO_199999.nameAr;
        } else if (constant.equalsIgnoreCase(FROM_MORE_200000.getConstant())) {
            return Helper.INSTANCE.isEnglish() ? FROM_MORE_200000.nameEn : FROM_MORE_200000.nameAr;
        }
        return constant;
    }


    public static StanderModel getObjectByConstant(String constant) {
        if (constant.equalsIgnoreCase(ZEROES.getConstant())) {
            return new StanderModel(ZEROES.constant, new LocalizedModel(ZEROES.nameAr, ZEROES.nameEn).getLocalized(), false);
        }else if (constant.equalsIgnoreCase(FROM_ZERO_TO_9999.getConstant())) {
            return new StanderModel(FROM_ZERO_TO_9999.constant, new LocalizedModel(FROM_ZERO_TO_9999.nameAr, FROM_ZERO_TO_9999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_10000_TO_19999.getConstant())) {
            return new StanderModel(FROM_10000_TO_19999.constant, new LocalizedModel(FROM_10000_TO_19999.nameAr, FROM_10000_TO_19999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_20000_TO_29999.getConstant())) {
            return new StanderModel(FROM_20000_TO_29999.constant, new LocalizedModel(FROM_20000_TO_29999.nameAr, FROM_20000_TO_29999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_30000_TO_39999.getConstant())) {
            return new StanderModel(FROM_30000_TO_39999.constant, new LocalizedModel(FROM_30000_TO_39999.nameAr, FROM_30000_TO_39999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_40000_TO_49999.getConstant())) {
            return new StanderModel(FROM_40000_TO_49999.constant, new LocalizedModel(FROM_40000_TO_49999.nameAr, FROM_40000_TO_49999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_50000_TO_59999.getConstant())) {
            return new StanderModel(FROM_50000_TO_59999.constant, new LocalizedModel(FROM_50000_TO_59999.nameAr, FROM_50000_TO_59999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_60000_TO_69999.getConstant())) {
            return new StanderModel(FROM_60000_TO_69999.constant, new LocalizedModel(FROM_60000_TO_69999.nameAr, FROM_60000_TO_69999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_70000_TO_79999.getConstant())) {
            return new StanderModel(FROM_70000_TO_79999.constant, new LocalizedModel(FROM_70000_TO_79999.nameAr, FROM_70000_TO_79999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_80000_TO_89999.getConstant())) {
            return new StanderModel(FROM_80000_TO_89999.constant, new LocalizedModel(FROM_80000_TO_89999.nameAr, FROM_80000_TO_89999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_90000_TO_99999.getConstant())) {
            return new StanderModel(FROM_90000_TO_99999.constant, new LocalizedModel(FROM_90000_TO_99999.nameAr, FROM_90000_TO_99999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_100000_TO_109999.getConstant())) {
            return new StanderModel(FROM_100000_TO_109999.constant, new LocalizedModel(FROM_100000_TO_109999.nameAr, FROM_100000_TO_109999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_110000_TO_119999.getConstant())) {
            return new StanderModel(FROM_110000_TO_119999.constant, new LocalizedModel(FROM_110000_TO_119999.nameAr, FROM_110000_TO_119999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_120000_TO_129999.getConstant())) {
            return new StanderModel(FROM_120000_TO_129999.constant, new LocalizedModel(FROM_120000_TO_129999.nameAr, FROM_120000_TO_129999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_130000_TO_139999.getConstant())) {
            return new StanderModel(FROM_130000_TO_139999.constant, new LocalizedModel(FROM_130000_TO_139999.nameAr, FROM_130000_TO_139999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_140000_TO_149999.getConstant())) {
            return new StanderModel(FROM_140000_TO_149999.constant, new LocalizedModel(FROM_140000_TO_149999.nameAr, FROM_140000_TO_149999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_150000_TO_159999.getConstant())) {
            return new StanderModel(FROM_150000_TO_159999.constant, new LocalizedModel(FROM_150000_TO_159999.nameAr, FROM_150000_TO_159999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_160000_TO_169999.getConstant())) {
            return new StanderModel(FROM_160000_TO_169999.constant, new LocalizedModel(FROM_160000_TO_169999.nameAr, FROM_160000_TO_169999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_170000_TO_179999.getConstant())) {
            return new StanderModel(FROM_170000_TO_179999.constant, new LocalizedModel(FROM_170000_TO_179999.nameAr, FROM_170000_TO_179999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_180000_TO_189999.getConstant())) {
            return new StanderModel(FROM_180000_TO_189999.constant, new LocalizedModel(FROM_180000_TO_189999.nameAr, FROM_180000_TO_189999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_190000_TO_199999.getConstant())) {
            return new StanderModel(FROM_190000_TO_199999.constant, new LocalizedModel(FROM_190000_TO_199999.nameAr, FROM_190000_TO_199999.nameEn).getLocalized(), false);
        } else if (constant.equalsIgnoreCase(FROM_MORE_200000.getConstant())) {
            return new StanderModel(FROM_MORE_200000.constant, new LocalizedModel(FROM_MORE_200000.nameAr, FROM_MORE_200000.nameEn).getLocalized(), false);
        }
        return null;
    }
}
